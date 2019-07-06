package com.example.haikalfluzain.nowadays.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.haikalfluzain.nowadays.R;
import com.example.haikalfluzain.nowadays.model.Today;

import java.util.ArrayList;
import java.util.List;

public class TodayAdapter extends RecyclerView.Adapter<TodayAdapter.Holder>{
    List<Today> todays = new ArrayList<>();
    Context context;
    View view;

    public TodayAdapter(Context context){
        this.context = context;
    }

    class  Holder extends RecyclerView.ViewHolder{
        TextView tv_start,tv_end,tv_activity;
        LinearLayout cv_item;

        public Holder(View itemView){
            super(itemView);
            tv_start = itemView.findViewById(R.id.tv_start);
            tv_end = itemView.findViewById(R.id.tv_end);
            tv_activity = itemView.findViewById(R.id.tv_activity);
            cv_item = itemView.findViewById(R.id.cv_item);
        }
    }

    @Override
    public TodayAdapter.Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(context).inflate(R.layout.today_list, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final TodayAdapter.Holder holder,final int i) {
        holder.tv_start.setText(todays.get(i).getStart());
        holder.tv_end.setText(todays.get(i).getEnd());
        holder.tv_activity.setText(todays.get(i).getActivity());
    }

    @Override
    public int getItemCount() {
        return todays.size();
    }

    public void replace_data(List<Today> todays)
    {
        this.todays = todays;
        notifyDataSetChanged();
    }

}

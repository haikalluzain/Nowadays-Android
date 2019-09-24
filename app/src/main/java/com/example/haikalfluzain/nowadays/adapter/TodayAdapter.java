package com.example.haikalfluzain.nowadays.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.haikalfluzain.nowadays.R;
import com.example.haikalfluzain.nowadays.model.Today;

import java.util.ArrayList;
import java.util.List;

public class TodayAdapter extends RecyclerView.Adapter<TodayAdapter.Holder> {

    List<Today> todays = new ArrayList<>();
    Context context;
    View view;
    Listener mListener;

    public TodayAdapter(Context context, Listener listener) {
        this.context = context;
        this.mListener = listener;
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView tv_start, tv_end, tv_activity;
        LinearLayout cv_item, dot, dotActive;

        public Holder(final View itemView) {
            super(itemView);
            tv_start = itemView.findViewById(R.id.tv_start);
            tv_end = itemView.findViewById(R.id.tv_end);
            tv_activity = itemView.findViewById(R.id.tv_activity);
            cv_item = itemView.findViewById(R.id.cv_item);
            dot = itemView.findViewById(R.id.dot);
            dotActive = itemView.findViewById(R.id.dot_active);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Today today = todays.get(getAdapterPosition());
                    mListener.onItemClick(today, itemView);


                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @SuppressLint("ResourceAsColor")
                @Override
                public boolean onLongClick(View v) {

                    Today today = todays.get(getAdapterPosition());
                    mListener.onLongItemClick(today, itemView);

                    Log.e("WAAA","WWW");

                    return true;
                }
            });
        }
    }

    @Override
    public TodayAdapter.Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(context).inflate(R.layout.today_list, viewGroup, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final TodayAdapter.Holder holder, final int i) {
        holder.tv_start.setText(todays.get(i).getStart());
        holder.tv_end.setText(todays.get(i).getEnd());
        holder.tv_activity.setText(todays.get(i).getActivity());

        Today today = todays.get(i);
        if (!today.isChecked()) {
            TypedValue outValue = new TypedValue();
            holder.itemView.getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            holder.cv_item.setBackgroundResource(outValue.resourceId);
            holder.dotActive.setVisibility(View.GONE);
            holder.dot.setVisibility(View.VISIBLE);
        } else {
            holder.cv_item.setBackgroundResource(R.color.grey_200);
            holder.dotActive.setVisibility(View.VISIBLE);
            holder.dot.setVisibility(View.GONE);

        }
    }

    @Override
    public int getItemCount() {
        return todays.size();
    }

    public void replace_data(List<Today> todays) {
        this.todays = todays;
        notifyDataSetChanged();
    }

    public interface Listener {
        void onItemClick(Today today, View view);

        void onLongItemClick(Today today, View view);
    }

}

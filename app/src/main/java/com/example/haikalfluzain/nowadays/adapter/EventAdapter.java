package com.example.haikalfluzain.nowadays.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.haikalfluzain.nowadays.R;
import com.example.haikalfluzain.nowadays.model.Event;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.Holder> {
    List<Event> events = new ArrayList<>();
    Context context;
    View view;
    Listener mListener;

    public EventAdapter(Context context, Listener listener){
        this.context = context;
        this.mListener = listener;
    }

    public class Holder extends RecyclerView.ViewHolder{
        LinearLayout mv, active;
        FrameLayout lv;
        TextView title,date;
        ImageView icon;

        public Holder(final View itemView) {
            super(itemView);
            lv = itemView.findViewById(R.id.lv);
            mv = itemView.findViewById(R.id.main_view);
            active = itemView.findViewById(R.id.dot_active);
            title = itemView.findViewById(R.id.title);
            date = itemView.findViewById(R.id.date);
            icon = itemView.findViewById(R.id.icon);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Event event = events.get(getAdapterPosition());
                    mListener.onItemClick(event, itemView);
                }
            });

            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    Event event = events.get(getAdapterPosition());
                    mListener.onLongItemClick(event, itemView);
                    return true;
                }
            });

        }
    }


    @Override
    public EventAdapter.Holder onCreateViewHolder(ViewGroup viewGroup, int i) {
        view = LayoutInflater.from(context).inflate(R.layout.event_list, viewGroup, false);
        return new Holder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onBindViewHolder(@NonNull EventAdapter.Holder holder, int i) {

        String start = events.get(i).getStart();
        String end = events.get(i).getEnd();

        start = start.replace("-","/");
        end = end.replace("-","/");
        SimpleDateFormat formatStart = new SimpleDateFormat("dd MMM");
        SimpleDateFormat formatEnd = new SimpleDateFormat("dd MMM yyyy");
        String newStart = formatStart.format(Date.parse(start));
        String newEnd = formatEnd.format(Date.parse(end));

        if (events.get(i).getTitle().equals("-")){
            holder.title.setText("( Tanpa Judul )");
        }else{
            holder.title.setText(events.get(i).getTitle());
        }

        holder.date.setText(newStart + " - " + newEnd);

        switch (events.get(i).getColor()){
            case "primary":
                holder.mv.getBackground().setColorFilter(context.getResources().getColor(R.color.blue_600), PorterDuff.Mode.SRC_ATOP);
                holder.icon.setColorFilter(context.getResources().getColor(R.color.white));
                holder.title.setTextColor(context.getResources().getColor(R.color.white));
                holder.date.setTextColor(context.getResources().getColor(R.color.white));
                break;
            case "success":
                holder.mv.getBackground().setColorFilter(context.getResources().getColor(R.color.green_100), PorterDuff.Mode.SRC_ATOP);
                holder.icon.setColorFilter(context.getResources().getColor(R.color.green_800));
                holder.title.setTextColor(context.getResources().getColor(R.color.green_800));
                holder.date.setTextColor(context.getResources().getColor(R.color.green_800));
                break;
            case "info":
                holder.mv.getBackground().setColorFilter(context.getResources().getColor(R.color.blue_100), PorterDuff.Mode.SRC_ATOP);
                holder.icon.setColorFilter(context.getResources().getColor(R.color.blue_800));
                holder.title.setTextColor(context.getResources().getColor(R.color.blue_800));
                holder.date.setTextColor(context.getResources().getColor(R.color.blue_800));
                break;
            case "warning":
                holder.mv.getBackground().setColorFilter(context.getResources().getColor(R.color.orange_100), PorterDuff.Mode.SRC_ATOP);
                holder.icon.setColorFilter(context.getResources().getColor(R.color.orange_800));
                holder.title.setTextColor(context.getResources().getColor(R.color.orange_800));
                holder.date.setTextColor(context.getResources().getColor(R.color.orange_800));
                break;
            case "danger":
                holder.mv.getBackground().setColorFilter(context.getResources().getColor(R.color.red_100), PorterDuff.Mode.SRC_ATOP);
                holder.icon.setColorFilter(context.getResources().getColor(R.color.red_800));
                holder.title.setTextColor(context.getResources().getColor(R.color.red_800));
                holder.date.setTextColor(context.getResources().getColor(R.color.red_800));
                break;
                default:
                    holder.mv.getBackground().setColorFilter(context.getResources().getColor(R.color.grey_100), PorterDuff.Mode.SRC_ATOP);
                    holder.icon.setColorFilter(context.getResources().getColor(R.color.grey_800));
                    holder.title.setTextColor(context.getResources().getColor(R.color.grey_800));
                    holder.date.setTextColor(context.getResources().getColor(R.color.grey_800));
                    break;
        }

        if (events.get(i).isChecked()){
            holder.lv.setForeground(new ColorDrawable(context.getResources().getColor(R.color.rgba)));
            holder.active.setVisibility(View.VISIBLE);
        }else{
            holder.lv.setForeground(new ColorDrawable(Color.TRANSPARENT));
            holder.active.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    public void replaceData(List<Event> events){
        this.events = events;
        notifyDataSetChanged();
    }

    public interface Listener{
        void onItemClick(Event event, View view);
        void onLongItemClick(Event event, View view);

    }
}

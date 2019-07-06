package com.example.haikalfluzain.nowadays.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.haikalfluzain.nowadays.activity.AddTodayDialog;
import com.example.haikalfluzain.nowadays.R;
import com.example.haikalfluzain.nowadays.activity.ProfileDialog;
import com.example.haikalfluzain.nowadays.adapter.TodayAdapter;
import com.example.haikalfluzain.nowadays.base.BaseFragment;
import com.example.haikalfluzain.nowadays.model.Today;
import com.example.haikalfluzain.nowadays.presenter.TodayPresenter;
import com.example.haikalfluzain.nowadays.view.TodayView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class TodayFragment extends BaseFragment implements TodayView {
    RecyclerView recyclerView;
    TodayAdapter todayAdapter;
    TodayPresenter todayPresenter;
    SwipeRefreshLayout refresh;
    ImageButton user, add;
    TextView count,date;
    LinearLayout noData;
    int dataCount = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today, container, false);
        recyclerView = view.findViewById(R.id.recyclerTest);
        refresh = view.findViewById(R.id.refresh);
        todayPresenter = new TodayPresenter(this);
        todayPresenter.getToday();
        refresh.setColorSchemeResources(R.color.blue_500);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                todayPresenter.getToday();
            }
        });
        todayAdapter = new TodayAdapter(getActivity());
        user = view.findViewById(R.id.user);
        add = view.findViewById(R.id.addToday);
        count = view.findViewById(R.id.countData);
        date = view.findViewById(R.id.date);
        noData = view.findViewById(R.id.noData);
        noData.setVisibility(View.GONE);

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ProfileDialog.class));
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddTodayDialog.class));
            }
        });

        recyclerView.setAdapter(todayAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        SimpleDateFormat df = new SimpleDateFormat("EEEE, d MMMM yyyy");
        Date d = new Date();
        String nowDate = df.format(d);
        date.setText(nowDate);


        return view;
    }

    @Override
    public void onSuccessLoadTodays(List<Today> todays) {
        todayAdapter.replace_data(todays);
        if (todayAdapter.getItemCount() != 0)
        {
            dataCount = todayAdapter.getItemCount();
            count.setText(dataCount + " Jadwal");
            noData.setVisibility(View.GONE);
        }else{
            count.setText("Belum ada Jadwal");
            noData.setVisibility(View.VISIBLE);
        }
        refresh.setRefreshing(false);
    }

    @Override
    public void onSuccessStore(String code, String message) {

    }

    @Override
    public void onShow() {
        refresh.setRefreshing(true);
    }

    @Override
    public void onHide() {

    }

    @Override
    public void getHttp(String http) {
        super.showHttp(http);
    }

    @Override
    public void getError(String error) {
        super.showError(error);
    }
}

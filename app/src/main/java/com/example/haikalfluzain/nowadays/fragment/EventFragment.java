package com.example.haikalfluzain.nowadays.fragment;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haikalfluzain.nowadays.R;
import com.example.haikalfluzain.nowadays.activity.AddEvent;
import com.example.haikalfluzain.nowadays.activity.ProfileDialog;
import com.example.haikalfluzain.nowadays.adapter.EventAdapter;
import com.example.haikalfluzain.nowadays.base.BaseFragment;
import com.example.haikalfluzain.nowadays.helper.OnBackPressed;
import com.example.haikalfluzain.nowadays.helper.SharedPrefManager;
import com.example.haikalfluzain.nowadays.model.Event;
import com.example.haikalfluzain.nowadays.presenter.EventPresenter;
import com.example.haikalfluzain.nowadays.view.EventView;
import com.whiteelephant.monthpicker.MonthPickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

public class EventFragment extends BaseFragment implements EventView, EventAdapter.Listener, OnBackPressed {
    LinearLayout changeMonth, noData, selectAll,activeList;
    CardView bottom_sheet;
    View color;
    FrameLayout mainList;
    RecyclerView recyclerView;
    NestedScrollView scrollView;
    EventAdapter eventAdapter;
    EventPresenter eventPresenter;
    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout refresh;
    TextView count, date, changeMonthText, selectAllText, title,desc,date_detail;
    ImageView changeMonthIcon, user, delete;
    ImageButton close_detail,hide, delete_detail;
    FloatingActionButton add;
    BottomSheetBehavior behavior;
    int dataCount, month, year, selected = 0;
    boolean showAll = false, isDetail = false;
    String nowdate, detail_id;
    ArrayList<String> selectArr = new ArrayList<>();
    List<Event> events = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event, container, false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(Color.WHITE);
        }

        Calendar c = Calendar.getInstance();
        month = c.get(Calendar.MONTH) + 1;
        year = c.get(Calendar.YEAR);
        nowdate = changeFormat(String.valueOf(new Date()));

        recyclerView = view.findViewById(R.id.recyclerTest);
        scrollView = view.findViewById(R.id.nestedScrollView);
        refresh = view.findViewById(R.id.refresh);
        sharedPrefManager = new SharedPrefManager(getContext());
        eventPresenter = new EventPresenter(this);
        eventPresenter.getEvent(sharedPrefManager.getSpToken(),month,year);
        eventAdapter = new EventAdapter(getActivity(),this);
        recyclerView.setAdapter(eventAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        date = view.findViewById(R.id.date);
        changeMonthText = view.findViewById(R.id.changeMonthText);
        changeMonthIcon = view.findViewById(R.id.changeMonthIcon);
        noData = view.findViewById(R.id.noData);
        noData.setVisibility(View.GONE);
        selectAll = view.findViewById(R.id.selectAll);
        selectAllText = view.findViewById(R.id.selectAllText);
        user = view.findViewById(R.id.user);
        delete = view.findViewById(R.id.deleteEvent);
        delete.setVisibility(View.GONE);
        add = view.findViewById(R.id.floatingAdd);
        close_detail = view.findViewById(R.id.close);
        hide = view.findViewById(R.id.hide);

        title = view.findViewById(R.id.title);
        desc = view.findViewById(R.id.desc);
        color = view.findViewById(R.id.color);
        date_detail = view.findViewById(R.id.date_detail);
        delete_detail = view.findViewById(R.id.delete_detail);

        bottom_sheet = view.findViewById(R.id.bottom_sheet);
        behavior = BottomSheetBehavior.from(bottom_sheet);
        behavior.setPeekHeight(0);
        behavior.setFitToContents(true);

        behavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int i) {
                switch (i) {
                    case BottomSheetBehavior.STATE_COLLAPSED | BottomSheetBehavior.STATE_HIDDEN:
                        behavior.setPeekHeight(0);
                        isDetail = false;
                        add.show();
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED:
                        behavior.setPeekHeight(900);
                        add.hide();
                        isDetail = true;
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) {
                if (v == 0.0){
                    add.show();
                }

                if (v < 1.0){
                    bottom_sheet.setRadius(50);
                }else {
                    bottom_sheet.setRadius(0);
                }
            }
        });
        close_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        });

        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                behavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            }
        });

        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toogleFilter(true);
        }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), AddEvent.class),2);
                getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                    if (scrollY > oldScrollY && add.getVisibility() == View.VISIBLE) {
                        add.hide();
                    } else if (scrollY < oldScrollY && add.getVisibility() != View.VISIBLE) {
                        add.show();
                    }
                }
            });
        }


        count = view.findViewById(R.id.count);
        changeMonth = view.findViewById(R.id.changeMonth);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (showAll){
                    eventPresenter.getEvent(sharedPrefManager.getSpToken(),0,0);
                }else{
                    eventPresenter.getEvent(sharedPrefManager.getSpToken(),month,year);
                }
                count.setText("Loading...");
                selected = 0;
                count.setTextColor(getResources().getColor(R.color.grey_800));
            }
        });

        // Initialize first Value
        date.setText(nowdate);
        final String[] Nmonth = nowdate.split(" ");
        changeMonthText.setText(Nmonth[0]);


        changeMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar today = Calendar.getInstance();
                MonthPickerDialog.Builder builder = new MonthPickerDialog.Builder(getContext(), new MonthPickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(int selectedMonth, int selectedYear) {
                        selectedMonth += 1;
                        nowdate = changeFormat(selectedYear + "-" + selectedMonth + "-" + "01");

                        month = selectedMonth;
                        year = selectedYear;
                        toogleFilter(false);

                        date.setText(nowdate);
                        String[] Nmonth = nowdate.split(" ");
                        changeMonthText.setText(Nmonth[0]);
                        Log.e(TAG, "Month : " + month + " Year : " + year);


                    }
                }, today.get(Calendar.YEAR), today.get(Calendar.MONTH));

                builder.setActivatedMonth(month -1)
                        .setActivatedYear(year)
                        .setTitle("Select Month to show")
                        .setOnMonthChangedListener(new MonthPickerDialog.OnMonthChangedListener() {
                            @Override
                            public void onMonthChanged(int selectedMonth) {
                                Log.d(TAG, "Selected month : " + selectedMonth);
                            }
                        })
                        .setOnYearChangedListener(new MonthPickerDialog.OnYearChangedListener() {
                            @Override
                            public void onYearChanged(int selectedYear) {
                                Log.d(TAG, "Selected year : " + selectedYear);
                            }
                        })
                        .build()
                        .show();

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Yakin ingin menghapus jadwal?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                for (int i = 0; i < events.size(); i++){
                                    Event event = events.get(i);
                                    if (event.isChecked()) {
                                        selectArr.add(event.getId());
                                    }

                                    events.get(i).setChecked(false);
                                    eventAdapter.notifyDataSetChanged();
                                }
                                String selectedId = TextUtils.join(",", selectArr);
                                eventPresenter.delete(selectedId,sharedPrefManager.getSpToken());
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                // Create the AlertDialog object and return it
                builder.show();
            }
        });

        delete_detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Yakin ingin menghapus jadwal?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                eventPresenter.delete(detail_id,sharedPrefManager.getSpToken());
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog
                            }
                        });
                // Create the AlertDialog object and return it
                builder.show();
            }
        });

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ProfileDialog.class));
            }
        });

        return view;
    }

    public String changeFormat(String date){
        date = date.replace("-","/");
        SimpleDateFormat format = new SimpleDateFormat("MMMM yyyy");
        String newdate = format.format(Date.parse(date));
        return newdate;
    }

    public void toogleFilter(boolean show){
        if (show) {
            showAll = true;
            selectAll.getBackground().setColorFilter(getContext().getResources().getColor(R.color.blue_600), PorterDuff.Mode.SRC_ATOP);
            selectAllText.setTextColor(getContext().getResources().getColor(R.color.white));
            eventPresenter.getEvent(sharedPrefManager.getSpToken(),0,0);
            date.setText("Semua Bulan");

            changeMonth.getBackground().setColorFilter(getContext().getResources().getColor(R.color.grey_200), PorterDuff.Mode.SRC_ATOP);
            changeMonthIcon.setColorFilter(getContext().getResources().getColor(R.color.grey_800));
            changeMonthText.setTextColor(getContext().getResources().getColor(R.color.grey_800));
        } else {
            showAll = false;
            selectAll.getBackground().setColorFilter(getContext().getResources().getColor(R.color.grey_200), PorterDuff.Mode.SRC_ATOP);
            selectAllText.setTextColor(getContext().getResources().getColor(R.color.grey_800));
            eventPresenter.getEvent(sharedPrefManager.getSpToken(),month,year);
            date.setText(changeFormat(year + "-" + month + "-01"));

            changeMonth.getBackground().setColorFilter(getContext().getResources().getColor(R.color.blue_600), PorterDuff.Mode.SRC_ATOP);
            changeMonthIcon.setColorFilter(getContext().getResources().getColor(R.color.white));
            changeMonthText.setTextColor(getContext().getResources().getColor(R.color.white));
        }
    }

    public void toogleAction(boolean isDelete){
        if (isDelete){
            delete.setVisibility(View.VISIBLE);
            selectAll.setVisibility(View.GONE);
            changeMonth.setVisibility(View.GONE);
        }else{
            delete.setVisibility(View.GONE);
            selectAll.setVisibility(View.VISIBLE);
            changeMonth.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSuccessLoadEvent(List<Event> events) {
        eventAdapter.replaceData(events);
        dataCount = eventAdapter.getItemCount();
        this.events = events;
        if (dataCount > 0){
            count.setText(dataCount + " Kegiatan");
            noData.setVisibility(View.GONE);
        }else{
            count.setText("Tidak ada kegiatan");
            noData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSuccessStoreEvent(String code, String message) {

    }

    @Override
    public void onSuccessDeleteEvent(String code, String message) {
        if (code.equals("200"))
        {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            selected = 0;
            count.setText(dataCount + " Kegiatan");
            count.setTextColor(getResources().getColor(R.color.grey_800));
            selectArr.removeAll(selectArr);
            toogleAction(false);
            eventPresenter.getEvent(sharedPrefManager.getSpToken(),month,year);

            if (behavior.getPeekHeight() > 0.0){
                behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            }
        }else{
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onShow() {
        refresh.setRefreshing(true);
    }

    @Override
    public void onHide() {
        refresh.setRefreshing(false);
    }

    @Override
    public void getHttp(String http) {
        super.showHttp(http);
    }

    @Override
    public void getError(String error) {
        super.showError(error);
    }

    @Override
    public void onItemClick(Event event, View view) {
        activeList = view.findViewById(R.id.dot_active);
        mainList = view.findViewById(R.id.lv);


        if (event.isChecked()){
            if (selected > 0){
                selected -= 1;
                if (selected == 0){
                    count.setText(dataCount + " Kegiatan");
                    count.setTextColor(getResources().getColor(R.color.grey_800));
                    toogleAction(false);
                }else {
                    count.setText(selected + "");
                    count.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
                event.setChecked(false);
            }else {
                if (isDetail){
                    behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }else{
                    showDetail(true,event.getId(),event.getTitle(),event.getDescription(),event.getStart(),event.getEnd(),event.getColor());
                }

            }
            mainList.setForeground(new ColorDrawable(Color.TRANSPARENT));
            activeList.setVisibility(View.GONE);

        }else{
            if (selected > 0){
                selected += 1;
                count.setText(selected + "");
                count.setTextColor(getResources().getColor(R.color.colorPrimary));
                mainList.setForeground(new ColorDrawable(getContext().getResources().getColor(R.color.rgba)));
                activeList.setVisibility(View.VISIBLE);
                event.setChecked(true);
            }else{
                if (isDetail){
                    behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
                }else{
                    showDetail(true,event.getId(),event.getTitle(),event.getDescription(),event.getStart(),event.getEnd(),event.getColor());
                }
            }

        }
    }

    @Override
    public void onLongItemClick(Event event, View view) {
        activeList = view.findViewById(R.id.dot_active);
        mainList = view.findViewById(R.id.lv);

        if (event.isChecked()){
            selected -= 1;
            count.setText(selected + "");
            count.setTextColor(getResources().getColor(R.color.colorPrimary));
            if (selected < 1){
                toogleAction(false);
//                add.setVisibility(View.VISIBLE);
                count.setText(dataCount + " Kegiatan");
                count.setTextColor(getResources().getColor(R.color.grey_800));
            }
            mainList.setForeground(new ColorDrawable(Color.TRANSPARENT));
            activeList.setVisibility(View.GONE);
            event.setChecked(false);

        }else{
            if (selected == -1){
                selected += 1;
            }
            selected += 1;
            count.setText(selected + "");
            count.setTextColor(getResources().getColor(R.color.colorPrimary));
            toogleAction(true);
            mainList.setForeground(new ColorDrawable(getContext().getResources().getColor(R.color.rgba)));
            activeList.setVisibility(View.VISIBLE);
            event.setChecked(true);
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2) {
            if(resultCode == RESULT_OK) {
                String check = data.getStringExtra("refresh");
                String newMonth = data.getStringExtra("month");
                String newYear = data.getStringExtra("year");
                if (check.equals("true")){
                    nowdate = changeFormat(newYear + "-" + newMonth + "-" + "01");

                    month = Integer.valueOf(newMonth);
                    year = Integer.valueOf(newYear);
                    toogleFilter(false);

                    date.setText(nowdate);
                    String[] Nmonth = nowdate.split(" ");
                    changeMonthText.setText(Nmonth[0]);
                    eventPresenter.getEvent(sharedPrefManager.getSpToken(), month, year);
                }
            }
        }
    }

    public void showDetail(boolean show,String id, String Gtitle,String Gdesc, String Gstart, String Gend, String Gcolor){
        if (show){
            behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            add.hide();
            title.setText(Gtitle);
            date_detail.setText(Gstart);
            detail_id = id;
            desc.setText(Gdesc);
            switch (Gcolor){
                case "primary":
                    color.getBackground().setColorFilter(getContext().getResources().getColor(R.color.blue_600), PorterDuff.Mode.SRC_ATOP);
                    break;
                case "success":
                    color.getBackground().setColorFilter(getContext().getResources().getColor(R.color.green_600), PorterDuff.Mode.SRC_ATOP);
                    break;
                case "info":
                    color.getBackground().setColorFilter(getContext().getResources().getColor(R.color.blue_200), PorterDuff.Mode.SRC_ATOP);
                    break;
                case "warning":
                    color.getBackground().setColorFilter(getContext().getResources().getColor(R.color.orange_600), PorterDuff.Mode.SRC_ATOP);
                    break;
                case "danger":
                    color.getBackground().setColorFilter(getContext().getResources().getColor(R.color.red_600), PorterDuff.Mode.SRC_ATOP);
                    break;
                default:
                    color.getBackground().setColorFilter(getContext().getResources().getColor(R.color.grey_600), PorterDuff.Mode.SRC_ATOP);
                    break;
            }
        }else{
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            add.show();
        }
    }

    @Override
    public void onBackPressed() {
        getActivity().getSupportFragmentManager().popBackStack();
        if (behavior.getPeekHeight() > 0.0){
            behavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        }else{
            Fragment fragment = new TodayFragment();
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.fragment_container, fragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        }

    }
}

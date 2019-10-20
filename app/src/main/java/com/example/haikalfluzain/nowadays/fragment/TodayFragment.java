package com.example.haikalfluzain.nowadays.fragment;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.TextInputEditText;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.haikalfluzain.nowadays.activity.AddTodayDialog;
import com.example.haikalfluzain.nowadays.R;
import com.example.haikalfluzain.nowadays.activity.ProfileDialog;
import com.example.haikalfluzain.nowadays.adapter.TodayAdapter;
import com.example.haikalfluzain.nowadays.base.BaseFragment;
import com.example.haikalfluzain.nowadays.helper.SharedPrefManager;
import com.example.haikalfluzain.nowadays.model.Today;
import com.example.haikalfluzain.nowadays.presenter.TodayPresenter;
import com.example.haikalfluzain.nowadays.view.TodayView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

@SuppressWarnings("deprecation")
public class TodayFragment extends BaseFragment implements TodayView, TodayAdapter.Listener{
    RecyclerView recyclerView;
    TodayAdapter todayAdapter;
    TodayPresenter todayPresenter;
    SharedPrefManager sharedPrefManager;
    SwipeRefreshLayout refresh;
    ImageButton user, add, delete;
    TextView count,date;
    TextInputEditText activity,start,end;
    BottomSheetDialog bottomSheetDialog;
    Button save;
    LinearLayout noData, cv_item, dot, dotActive;
    int dataCount = 0;
    Integer selected = 0;
    ArrayList<String> selectArr = new ArrayList<>();
    List<Today> todays = new ArrayList<>();
    NoConnectionFragment noConnectionFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_today, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getActivity().getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.getDecorView().setSystemUiVisibility(0);
            window.setStatusBarColor(getResources().getColor(R.color.status));
        }

        recyclerView = view.findViewById(R.id.recyclerTest);
        refresh = view.findViewById(R.id.refresh);
        sharedPrefManager = new SharedPrefManager(getContext());
        todayPresenter = new TodayPresenter(this, getContext());
        todayPresenter.getToday(sharedPrefManager.getSpToken());
        todayAdapter = new TodayAdapter(getActivity(), this);
        recyclerView.setAdapter(todayAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        refresh.setColorSchemeResources(R.color.blue_500);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (selected == 0){
                    delete.setVisibility(View.GONE);
                }else{
                    delete.setVisibility(View.VISIBLE);
                }
                selected = 0;
                count.setTextColor(getResources().getColor(R.color.grey_700));
                delete.setVisibility(View.GONE);
                add.setVisibility(View.VISIBLE);
                todayPresenter.getToday(sharedPrefManager.getSpToken());

            }
        });
        user = view.findViewById(R.id.user);
        add = view.findViewById(R.id.addToday);
        count = view.findViewById(R.id.countData);
        date = view.findViewById(R.id.date);
        noData = view.findViewById(R.id.noData);
        noData.setVisibility(View.GONE);
        delete = view.findViewById(R.id.deleteToday);
        delete.setVisibility(View.GONE);


        bottomSheetDialog = new BottomSheetDialog(getActivity());
        bottomSheetDialog.setContentView(R.layout.bottom_today);

        noConnectionFragment = new NoConnectionFragment();

        user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), ProfileDialog.class));
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getActivity(), AddTodayDialog.class),1);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setMessage("Yakin ingin menghapus jadwal?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                for (int i = 0; i < todays.size(); i++){
                                    Today today = todays.get(i);
                                    if (today.isChecked()) {
                                        selectArr.add(today.getId());
                                    }

                                    todays.get(i).setChecked(false);
                                    todayAdapter.notifyDataSetChanged();
                                }
                                String selectedId = TextUtils.join(",", selectArr);
                                todayPresenter.delete(selectedId,sharedPrefManager.getSpToken());
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

        SimpleDateFormat df = new SimpleDateFormat("EEEE, d MMMM yyyy");
        Date d = new Date();
        String nowDate = df.format(d);
        date.setText(nowDate);


        return view;
    }

    @Override
    public void onSuccessLoadTodays(List<Today> todays) {
        todayAdapter.replace_data(todays);
        this.todays = todays;
        if (todayAdapter.getItemCount() != 0)
        {
            dataCount = todayAdapter.getItemCount();
            count.setText(dataCount + " Jadwal");
            noData.setVisibility(View.GONE);
        }else{
            count.setText("Belum ada Jadwal");
            noData.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onSuccessStore(String code, String message) {

    }

    @Override
    public void onSuccessUpdate(String code, String message) {
        if (code.equals("200"))
        {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            todayPresenter.getToday(sharedPrefManager.getSpToken());
        }else{
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }
        bottomSheetDialog.hide();
    }

    @Override
    public void onSuccessDelete(String code, String message) {
        if (code.equals("200"))
        {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            selected = 0;
            selectArr.removeAll(selectArr);
            count.setTextColor(getResources().getColor(R.color.grey_700));
            delete.setVisibility(View.GONE);
            add.setVisibility(View.VISIBLE);
            todayPresenter.getToday(sharedPrefManager.getSpToken());
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
        noConnectionFragment.show(getFragmentManager(), "");
        super.showError(error);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if(resultCode == RESULT_OK) {
                String check = data.getStringExtra("refresh");
                if (check.equals("true")){
                    todayPresenter.getToday(sharedPrefManager.getSpToken());
                }
            }
        }
    }

    @Override
    public void onItemClick(final Today today, View itemView) {
        cv_item = itemView.findViewById(R.id.cv_item);
        dot = itemView.findViewById(R.id.dot);
        dotActive = itemView.findViewById(R.id.dot_active);

        if (today.isChecked()){
            if (selected > 0){
                selected -= 1;
                if (selected == 0){
                    count.setText(dataCount + " Jadwal");
                    count.setTextColor(getResources().getColor(R.color.grey_700));
                    delete.setVisibility(View.GONE);
                    add.setVisibility(View.VISIBLE);
                }else {
                    count.setText(selected + "");
                    count.setTextColor(getResources().getColor(R.color.colorPrimary));
                }
                today.setChecked(false);
            }else {
                bottomSheetDialog.show();
            }
            TypedValue outValue = new TypedValue();
            itemView.getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            cv_item.setBackgroundResource(outValue.resourceId);
            dotActive.setVisibility(View.GONE);
            dot.setVisibility(View.VISIBLE);

        }else{
            if (selected > 0){
                selected += 1;
                count.setText(selected + "");
                count.setTextColor(getResources().getColor(R.color.colorPrimary));
                cv_item.setBackgroundResource(R.color.grey_200);
                dotActive.setVisibility(View.VISIBLE);
                dot.setVisibility(View.GONE);
                today.setChecked(true);
            }else{
                bottomSheetDialog.show();
            }


        }

        activity = bottomSheetDialog.findViewById(R.id.activity);
        start = bottomSheetDialog.findViewById(R.id.start);
        end = bottomSheetDialog.findViewById(R.id.end);

        start.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    String[] time = start.getText().toString().split(":");
                    start.setInputType(InputType.TYPE_NULL); // disable keyboard
                    TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            start.setText(String.format("%02d:%02d",hourOfDay,minute));
                            start.clearFocus();
                        }
                    }, Integer.valueOf(time[0]), Integer.valueOf(time[1]), true);
                    timePickerDialog.show();
                    timePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            start.clearFocus();
                        }
                    });
                }
            }
        });

        end.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    end.setInputType(InputType.TYPE_NULL); // disable keyboard
                    String[] time = end.getText().toString().split(":");
                    TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            end.setText(String.format("%02d:%02d", hourOfDay, minute));
                            end.clearFocus();
                        }
                    }, Integer.valueOf(time[0]), Integer.valueOf(time[1]), true);
                    timePickerDialog.show();
                    timePickerDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            end.clearFocus();
                        }
                    });
                }
            }
        });
        save = bottomSheetDialog.findViewById(R.id.save);

        activity.setText(today.getActivity());
        start.setText(today.getStart());
        end.setText(today.getEnd());

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Nactivity,Nstart, Nend;
                Nactivity = activity.getText().toString();
                Nstart = start.getText().toString();
                Nend = end.getText().toString();

                Date tstart = null,tend = null;

                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");
                try {
                    tstart = timeFormat.parse(Nstart);
                    tend = timeFormat.parse(Nend);
                } catch (ParseException e) {
                }
                if (Nactivity.isEmpty()) {
                    activity.setError("Please input activity!");
                    activity.requestFocus();
                    activity.clearComposingText();
                    return;
                }else if(Nstart.isEmpty()) {
                    start.setError("Please input time start!");
                    start.requestFocus();
                    start.clearComposingText();
                    return;
                }else if(Nend.isEmpty()) {
                    end.setError("Please input time end!");
                    end.requestFocus();
                    end.clearComposingText();
                    return;
                }else if(tstart.after(tend)){
                    Toast.makeText(getContext(), "Time start can't be set after time end!", Toast.LENGTH_SHORT).show();
                    start.setError("");
                    start.requestFocus();
                    start.clearComposingText();
                }else{
                    todayPresenter.update(Nactivity,today.getId(),Nstart,Nend,sharedPrefManager.getSpToken());
                }

            }
        });




    }

    @Override
    public void onLongItemClick(Today today, View itemView) {
        cv_item = itemView.findViewById(R.id.cv_item);
        dot = itemView.findViewById(R.id.dot);
        dotActive = itemView.findViewById(R.id.dot_active);

        if (today.isChecked()){
            selected -= 1;
            count.setText(selected + "");
            count.setTextColor(getResources().getColor(R.color.colorPrimary));
            if (selected < 1){
                delete.setVisibility(View.GONE);
                add.setVisibility(View.VISIBLE);
                count.setText(dataCount + " Jadwal");
                count.setTextColor(getResources().getColor(R.color.grey_700));
            }
            TypedValue outValue = new TypedValue();
            itemView.getContext().getTheme().resolveAttribute(android.R.attr.selectableItemBackground, outValue, true);
            cv_item.setBackgroundResource(outValue.resourceId);
            dotActive.setVisibility(View.GONE);
            dot.setVisibility(View.VISIBLE);
            today.setChecked(false);

        }else{
            if (selected == -1){
                selected += 1;
            }
            selected += 1;
            count.setText(selected + "");
            count.setTextColor(getResources().getColor(R.color.colorPrimary));
            delete.setVisibility(View.VISIBLE);
            add.setVisibility(View.GONE);
            cv_item.setBackgroundResource(R.color.grey_200);
            dotActive.setVisibility(View.VISIBLE);
            dot.setVisibility(View.GONE);
            today.setChecked(true);
        }
    }
}

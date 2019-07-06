package com.example.haikalfluzain.nowadays.activity;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.haikalfluzain.nowadays.R;
import com.example.haikalfluzain.nowadays.base.BaseActivity;
import com.example.haikalfluzain.nowadays.fragment.TimePickerFragment;
import com.example.haikalfluzain.nowadays.helper.SharedPrefManager;
import com.example.haikalfluzain.nowadays.model.Today;
import com.example.haikalfluzain.nowadays.presenter.TodayPresenter;
import com.example.haikalfluzain.nowadays.view.TodayView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddTodayDialog extends BaseActivity implements TodayView, TimePickerDialog.OnTimeSetListener {
    TodayPresenter todayPresenter;
    SharedPrefManager sharedPrefManager;
    TextInputEditText act,start,end;
    Button submit;
    boolean startClick = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL, WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH, WindowManager.LayoutParams.FLAG_WATCH_OUTSIDE_TOUCH);

        setContentView(R.layout.activity_add_today_dialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        todayPresenter = new TodayPresenter(this);
        sharedPrefManager = new SharedPrefManager(this);



        act = findViewById(R.id.activity);
        start = findViewById(R.id.start);
        end = findViewById(R.id.end);
        submit = findViewById(R.id.submit);



        start.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    startClick = true;
                    DialogFragment timePicker = new TimePickerFragment();
                    timePicker.show(getSupportFragmentManager(), "time picker");
                }
            }
        });

        end.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    startClick = false;
                    DialogFragment timePicker = new TimePickerFragment();
                    timePicker.show(getSupportFragmentManager(), "time picker");
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nact = act.getText().toString();
                String nstart = start.getText().toString();
                String nend = end.getText().toString();

                Date tstart = null,tend = null;

                SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm");
                try {
                    tstart = timeFormat.parse(nstart);
                    tend = timeFormat.parse(nend);
                } catch (ParseException e) {
                }
                if (nact.isEmpty()) {
                    act.setError("Please input activity!");
                    act.requestFocus();
                    act.clearComposingText();
                    return;
                }else if(nstart.isEmpty()) {
                    start.setError("Please input time start!");
                    start.requestFocus();
                    start.clearComposingText();
                    return;
                }else if(nend.isEmpty()) {
                    end.setError("Please input time end!");
                    end.requestFocus();
                    end.clearComposingText();
                    return;
                }else if(tstart.after(tend)){
                    Toast.makeText(AddTodayDialog.this, "Time start can't be set after time end!", Toast.LENGTH_SHORT).show();
                    start.setError("");
                    start.clearComposingText();
                }else{
                    todayPresenter.store(nact,nstart,nend,sharedPrefManager.getSpToken());
                }
            }
        });


    }

    @Override
    public void onSuccessLoadTodays(List<Today> todays) {

    }

    @Override
    public void onSuccessStore(String code, String message) {
        if (code.equals("200"))
        {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            startActivity(new Intent(AddTodayDialog.this, Main.class));
            finish();
        }else{
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onShow() {
        super.showLoading("","Loading...");
    }

    @Override
    public void onHide() {
        super.dismissLoading();
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
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (MotionEvent.ACTION_OUTSIDE == event.getAction()){
            return true;
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        if (startClick){
            start.setText(String.format("%02d:%02d",hourOfDay,minute));
            start.clearFocus();
        }else {
            end.setText(String.format("%02d:%02d",hourOfDay,minute));
            end.clearFocus();
        }
    }

}

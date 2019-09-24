package com.example.haikalfluzain.nowadays.activity;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.haikalfluzain.nowadays.R;
import com.example.haikalfluzain.nowadays.base.BaseActivity;
import com.example.haikalfluzain.nowadays.helper.SharedPrefManager;
import com.example.haikalfluzain.nowadays.model.Today;
import com.example.haikalfluzain.nowadays.presenter.TodayPresenter;
import com.example.haikalfluzain.nowadays.view.TodayView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static android.content.DialogInterface.*;

public class AddTodayDialog extends BaseActivity implements TodayView{
    TodayPresenter todayPresenter;
    SharedPrefManager sharedPrefManager;
    TextInputEditText act,start,end;
    Button submit,cancel;
    public boolean startClick = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.setFinishOnTouchOutside(false);

        setContentView(R.layout.activity_add_today_dialog);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        todayPresenter = new TodayPresenter(this);
        sharedPrefManager = new SharedPrefManager(this);



        act = findViewById(R.id.activity);
        start = findViewById(R.id.start);
        end = findViewById(R.id.end);
        submit = findViewById(R.id.submit);
        cancel = findViewById(R.id.cancel);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        start.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    startClick = true;
                    start.setInputType(InputType.TYPE_NULL); // disable keyboard
                    Calendar c = Calendar.getInstance();
                    int hour = c.get(Calendar.HOUR_OF_DAY);
                    int minute = c.get(Calendar.MINUTE);
                    TimePickerDialog timePickerDialog = new TimePickerDialog(AddTodayDialog.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            start.setText(String.format("%02d:%02d",hourOfDay,minute));
                            start.clearFocus();
                        }
                    }, hour, minute, true);
                    timePickerDialog.show();
                    timePickerDialog.setOnDismissListener(new OnDismissListener() {
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
                    startClick = false;
                    end.setInputType(InputType.TYPE_NULL); // disable keyboard
                    Calendar c = Calendar.getInstance();
                    int hour = c.get(Calendar.HOUR_OF_DAY);
                    int minute = c.get(Calendar.MINUTE);
                    TimePickerDialog timePickerDialog = new TimePickerDialog(AddTodayDialog.this, new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            end.setText(String.format("%02d:%02d", hourOfDay, minute));
                            end.clearFocus();
                        }
                    }, hour, minute, true);
                    timePickerDialog.show();
                    timePickerDialog.setOnDismissListener(new OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            end.clearFocus();
                        }
                    });
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
                    start.requestFocus();
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
            Intent intent = new Intent();
            intent.putExtra("refresh", "true");
            setResult(RESULT_OK, intent);
            finish();
        }else{
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSuccessUpdate(String code, String message) {

    }

    @Override
    public void onSuccessDelete(String code, String message) {

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

}

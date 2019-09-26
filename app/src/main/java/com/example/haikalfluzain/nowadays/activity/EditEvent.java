package com.example.haikalfluzain.nowadays.activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.haikalfluzain.nowadays.R;
import com.example.haikalfluzain.nowadays.helper.SharedPrefManager;
import com.example.haikalfluzain.nowadays.model.Event;
import com.example.haikalfluzain.nowadays.presenter.EventPresenter;
import com.example.haikalfluzain.nowadays.view.EventView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EditEvent extends AppCompatActivity implements EventView {
    private DatePickerDialog datePickerDialog;
    private SimpleDateFormat dateFormatter,df;
    SharedPrefManager sharedPrefManager;
    EditText title, desc;
    Button btn_start, btn_end, save;
    ImageButton back, primary, success, info, warning, danger;
    EventPresenter eventPresenter;
    Date nstart, nend;
    String start,end, color = "primary", month, year, id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_event);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(Color.WHITE);
        }

        title = findViewById(R.id.title);
        desc = findViewById(R.id.desc);
        primary = findViewById(R.id.primary);
        success = findViewById(R.id.success);
        info = findViewById(R.id.info);
        warning = findViewById(R.id.warning);
        danger = findViewById(R.id.danger);
        save = findViewById(R.id.save);

        id = getIntent().getStringExtra("id");
        String stitle = getIntent().getStringExtra("title");
        String sdesc = getIntent().getStringExtra("desc");
        if (stitle.equals("-")){
            title.setText("");
        }else{
            title.setText(stitle);
        }

        if (sdesc.equals("-")){
            desc.setText("");
        }else{
            desc.setText(sdesc);
        }

        start = getIntent().getStringExtra("start");
        end = getIntent().getStringExtra("end");
        colorPicker(getIntent().getStringExtra("color"));

        sharedPrefManager = new SharedPrefManager(this);
        eventPresenter = new EventPresenter(this);

        btn_start = findViewById(R.id.start);
        btn_end = findViewById(R.id.end);
        save = findViewById(R.id.save);

        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        title.requestFocus();
        if (title.hasFocus()){
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
        title.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        title.setRawInputType(InputType.TYPE_CLASS_TEXT);

        df = new SimpleDateFormat("yyyy-MM-dd");
        dateFormatter = new SimpleDateFormat("EEEE, d MMMM yyyy");
        try {
            nstart = df.parse(getIntent().getStringExtra("start"));
            nend = df.parse(getIntent().getStringExtra("end"));

            String[] date = df.format(nstart).split("-");
            year = date[0];
            month = date[1];

            btn_start.setHint(dateFormatter.format(nstart));
            btn_end.setHint(dateFormatter.format(nend));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                df = new SimpleDateFormat("yyyy-M-d");
                String[] Ndate = df.format(nstart).split("-");
                showDateDialog(true, Ndate[0],Ndate[1],Ndate[2]);
            }
        });

        btn_end.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                df = new SimpleDateFormat("yyyy-M-d");
                String[] Ndate = df.format(nend).split("-");
                showDateDialog(false, Ndate[0],Ndate[1],Ndate[2]);
            }
        });

        primary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorPicker("primary");
            }
        });

        info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorPicker("info");
            }
        });

        success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorPicker("success");
            }
        });

        warning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorPicker("warning");
            }
        });

        danger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                colorPicker("danger");
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ntitle = title.getText().toString();
                String ndesc = desc.getText().toString();
                if (ntitle.isEmpty()){
                    ntitle = "-";
                }

                if (ndesc.isEmpty()){
                    ndesc = "-";
                }

                if (nstart.after(nend)){
                    Toast.makeText(EditEvent.this, "Waktu mulai melebihi waktu selesai", Toast.LENGTH_SHORT).show();
                }else {
                    Log.e("Y",ntitle + "-" +ndesc + "-" + start + "-" + end + "-" + color);
                    eventPresenter.update(sharedPrefManager.getSpToken(),id,ntitle,ndesc,start,end,color);
                }


            }
        });
    }

    private void showDateDialog(final boolean isStart, String iyear, String imonth, String idate){
        dateFormatter = new SimpleDateFormat("EEEE, d MMMM yyyy");
        df = new SimpleDateFormat("yyyy-MM-dd");
        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int syear, int monthOfYear, int dayOfMonth) {

                Calendar newDate = Calendar.getInstance();
                newDate.set(syear, monthOfYear, dayOfMonth);
                try {
                    if (isStart){
                        btn_start.setHint(dateFormatter.format(newDate.getTime()));
                        nstart= df.parse(df.format(newDate.getTime()));
                        start = df.format(newDate.getTime());
                        btn_start.setHintTextColor(getResources().getColor(R.color.grey_700));
                        String[] date = df.format(nstart).split("-");
                        year = date[0];
                        month = date[1];
                        if (nstart.after(nend)){
                            nend = df.parse(df.format(newDate.getTime()));
                            end = df.format(newDate.getTime());
                            btn_end.setHint(dateFormatter.format(newDate.getTime()));
                        }

                    }else{
                        btn_end.setHint(dateFormatter.format(newDate.getTime()));
                        nend = df.parse(df.format(newDate.getTime()));
                        end = df.format(newDate.getTime());
                        if (nstart.after(nend)){
                            btn_start.setHintTextColor(getResources().getColor(R.color.red_500));
                        }else{
                            btn_start.setHintTextColor(getResources().getColor(R.color.grey_700));
                        }
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }


            }

        },Integer.valueOf(iyear), Integer.valueOf(imonth) - 1, Integer.valueOf(idate));

        datePickerDialog.show();
    }

    private String colorPicker(String colorIn){
        switch (colorIn){
            case "primary":
                primary.setImageResource(R.drawable.ic_check_black_24dp);
                info.setImageResource(0);
                warning.setImageResource(0);
                success.setImageResource(0);
                danger.setImageResource(0);
                break;
            case "info":
                info.setImageResource(R.drawable.ic_check_black_24dp);
                primary.setImageResource(0);
                warning.setImageResource(0);
                success.setImageResource(0);
                danger.setImageResource(0);
                break;
            case "success":
                success.setImageResource(R.drawable.ic_check_black_24dp);
                info.setImageResource(0);
                warning.setImageResource(0);
                primary.setImageResource(0);
                danger.setImageResource(0);
                break;
            case "warning":
                warning.setImageResource(R.drawable.ic_check_black_24dp);
                info.setImageResource(0);
                primary.setImageResource(0);
                success.setImageResource(0);
                danger.setImageResource(0);
                break;
            case "danger":
                danger.setImageResource(R.drawable.ic_check_black_24dp);
                info.setImageResource(0);
                warning.setImageResource(0);
                success.setImageResource(0);
                primary.setImageResource(0);
                break;
        }
        return color = colorIn;
    }

    @Override
    public void onSuccessLoadEvent(List<Event> events) {

    }

    @Override
    public void onSuccessStoreEvent(String code, String message) {

    }

    @Override
    public void onSuccessDeleteEvent(String code, String message) {

    }

    @Override
    public void onSuccessUpdateEvent(String code, String message) {
        if (code.equals("200"))
        {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.putExtra("refresh", "true");
            intent.putExtra("month", month);
            intent.putExtra("year", year);
            setResult(RESULT_OK, intent);
            finish();
        }else{
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onShow() {

    }

    @Override
    public void onHide() {

    }

    @Override
    public void getHttp(String http) {

    }

    @Override
    public void getError(String error) {

    }
}

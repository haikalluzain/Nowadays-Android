package com.example.haikalfluzain.nowadays.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.example.haikalfluzain.nowadays.R;
import com.example.haikalfluzain.nowadays.helper.SharedPrefManager;

public class ChangePort extends AppCompatActivity {
    TextInputEditText port;
    Button set,cancel;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_port);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        sharedPrefManager = new SharedPrefManager(this);

        set = findViewById(R.id.set);
        cancel = findViewById(R.id.cancel);

        port = findViewById(R.id.port);
        port.setText(sharedPrefManager.getIpAddress());
        port.requestFocus();

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedPrefManager.saveSPString(SharedPrefManager.IP_ADDRESS, port.getText().toString());
                closeKeyboard();
                finish();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyboard();
                finish();
            }
        });
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}

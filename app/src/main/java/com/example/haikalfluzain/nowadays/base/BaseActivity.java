package com.example.haikalfluzain.nowadays.base;

import android.app.ProgressDialog;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class BaseActivity extends AppCompatActivity {
    ProgressDialog progressDialog;


    public void setUpActionBar(String title)
    {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);
    }

    public void setHideActionBar()
    {
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
    }

    public void showError(String error)
    {
        Log.e("HTTP ERROR : ",error);
    }

    public void showMessage(String message) {
        Log.e("MESSAGE : ", message);
    }


    public void showHttp(String http)
    {
        Log.e("HTTP STATUS : ",http);
    }

    public void showLoading(String title, String message)
    {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle(title);
        progressDialog.setMessage(message);
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void dismissLoading()
    {
        progressDialog.dismiss();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}

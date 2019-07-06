package com.example.haikalfluzain.nowadays.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

public class BaseFragment extends Fragment {

    public View view;
    ProgressDialog progressDialog;

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

    public void showLoading(Context context)
    {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Tunggu Sebentar...");
        progressDialog.setCancelable(false);
        progressDialog.show();
    }

    public void dismissLoading()
    {
        progressDialog.dismiss();
    }
}

package com.example.haikalfluzain.nowadays.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.haikalfluzain.nowadays.R;
import com.example.haikalfluzain.nowadays.base.BaseActivity;
import com.example.haikalfluzain.nowadays.fragment.TodayFragment;
import com.example.haikalfluzain.nowadays.helper.SharedPrefManager;
import com.example.haikalfluzain.nowadays.presenter.AuthPresenter;
import com.example.haikalfluzain.nowadays.view.AuthView;

public class ProfileDialog extends BaseActivity implements AuthView {
    SharedPrefManager sharedPrefManager;
    AuthPresenter authPresenter;
    Button logout;
    TextView uname,uemail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_dialog);

        sharedPrefManager = new SharedPrefManager(this);
        authPresenter = new AuthPresenter(this);

        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        logout = findViewById(R.id.logout);

        uname = findViewById(R.id.name);
        uemail = findViewById(R.id.email);

        authPresenter.load(sharedPrefManager.getSpToken());

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                authPresenter.logout(sharedPrefManager.getSpToken());
            }
        });
    }

    @Override
    public void onSuccessLogin(String code, String message, String id, String token) {

    }

    @Override
    public void onSuccessLogout(String code, String message) {
        if (code.equals("200"))
        {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_LOGIN, false);
            startActivity(new Intent(ProfileDialog.this, Login.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
    }

    @Override
    public void onSuccessShowUser(String id, String name, String email) {
        uname.setText(name);
        uemail.setText(email);
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

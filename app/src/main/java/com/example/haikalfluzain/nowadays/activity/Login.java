package com.example.haikalfluzain.nowadays.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.TextInputEditText;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.haikalfluzain.nowadays.R;
import com.example.haikalfluzain.nowadays.base.BaseActivity;
import com.example.haikalfluzain.nowadays.helper.SharedPrefManager;
import com.example.haikalfluzain.nowadays.presenter.AuthPresenter;
import com.example.haikalfluzain.nowadays.view.AuthView;

public class Login extends BaseActivity implements AuthView {
    TextInputEditText email,password;
    Button login, skip;
    SharedPrefManager sharedPrefManager;
    AuthPresenter authPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.setStatusBarColor(Color.WHITE);
        }

        sharedPrefManager = new SharedPrefManager(this);
        authPresenter = new AuthPresenter(this);

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        skip = findViewById(R.id.skip);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Nemail,Npass;
                Nemail = email.getText().toString();
                Npass = password.getText().toString();

                if (Nemail.isEmpty()) {
                    email.setError("Please input your email!");
                    email.requestFocus();
                    email.clearComposingText();
                    return;
                }else if(!Patterns.EMAIL_ADDRESS.matcher(Nemail).matches()){
                    email.setError("This field format must be an email!");
                    email.requestFocus();
                    email.clearComposingText();
                    return;
                }else if (Npass.isEmpty()){
                    password.setError("Please input your password!");
                    password.requestFocus();
                    email.clearComposingText();
                    return;
                }else{
                    authPresenter.login(Nemail,Npass);
                }
            }
        });

        if (sharedPrefManager.getSpLogin()){
            startActivity(new Intent(Login.this, Main.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
    }

    @Override
    public void onSuccessLogin(String code, String message, String id, String token) {
        if (code.equals("200"))
        {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
            Intent i = new Intent(Login.this, Main.class);
            startActivity(i);
            sharedPrefManager.saveSPBoolean(SharedPrefManager.SP_LOGIN, true);
            sharedPrefManager.saveSPString(SharedPrefManager.SP_ID, id);
            sharedPrefManager.saveSPString(SharedPrefManager.SP_TOKEN, token);
        }else{
            Toast.makeText(this, "Username or Password is invalid", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onSuccessLogout(String code, String message) {

    }

    @Override
    public void onSuccessShowUser(String id, String name, String email) {

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
}

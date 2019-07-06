package com.example.haikalfluzain.nowadays.presenter;

import com.example.haikalfluzain.nowadays.base.BasePresenter;
import com.example.haikalfluzain.nowadays.base.BaseResponse;
import com.example.haikalfluzain.nowadays.response.LoginResponse;
import com.example.haikalfluzain.nowadays.response.UserResponse;
import com.example.haikalfluzain.nowadays.view.AuthView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthPresenter<authView extends AuthView> extends BasePresenter {

    authView authView;

    public AuthPresenter(authView authView)
    {
        this.authView = authView;
    }

    public void login(String email, String password)
    {
        authView.onShow();

        apiClass.login(email,password).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful())
                {
                    String code = response.body().getCode();
                    String message = response.body().getMessage();
                    String id = response.body().getId();
                    String token = response.body().getToken();
                    authView.onSuccessLogin(code,message,id,token);
                    authView.onHide();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                authView.getError(t.getMessage());
                authView.onHide();
            }
        });
    }

    public void load(String token)
    {
        String Ntoken =  "Bearer " + token;

        apiClass.loadAuth(Ntoken).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful())
                {
                    authView.onSuccessShowUser(response.body().getId(),response.body().getName(),response.body().getEmail());
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                authView.getError(t.getMessage());
            }
        });
    }

    public  void logout(String token)
    {
        authView.onShow();

        String Ntoken =  "Bearer " + token;

        apiClass.logout(Ntoken).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                if (response.isSuccessful())
                {
                    authView.onSuccessLogout(response.body().getCode(),response.body().getMessage());
                    authView.onHide();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                authView.getError(t.getMessage());
                authView.onHide();
            }
        });
    }
}

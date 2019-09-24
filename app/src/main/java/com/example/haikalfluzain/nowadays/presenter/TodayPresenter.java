package com.example.haikalfluzain.nowadays.presenter;

import android.util.Log;

import com.example.haikalfluzain.nowadays.base.BasePresenter;
import com.example.haikalfluzain.nowadays.base.BaseResponse;
import com.example.haikalfluzain.nowadays.response.TodayResponse;
import com.example.haikalfluzain.nowadays.view.TodayView;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodayPresenter<TV extends TodayView> extends BasePresenter {
    TV todayview;

    public TodayPresenter(TV todayview){
        this.todayview = todayview;
    }

    public void getToday(String token)
    {
        String Ntoken = "Bearer " + token;
        todayview.onShow();
        apiClass.getToday(Ntoken).enqueue(new Callback<TodayResponse>() {
            @Override
            public void onResponse(Call<TodayResponse> call, Response<TodayResponse> response) {
                todayview.getHttp(Integer.toString(response.code()));
                if (response.isSuccessful()){
                    todayview.onSuccessLoadTodays(response.body().getTodays());
                    todayview.onHide();
                }
            }

            @Override
            public void onFailure(Call<TodayResponse> call, Throwable t) {
                todayview.getError(t.getMessage());
                todayview.onHide();
            }

        });
    }

    public void store(String activity, String start, String end, String token)
    {
        todayview.onShow();
        String Ntoken = "Bearer " + token;

        apiClass.storeToday(Ntoken, activity,start,end).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Log.e("HTTP", response.message());
                if (response.isSuccessful())
                {
                    todayview.onSuccessStore(response.body().getCode(), response.body().getMessage());
                    todayview.onHide();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                todayview.getError(t.getMessage());
                todayview.onHide();
                Log.e("Error",t.getMessage());
            }
        });
    }

    public void update(String activity, String id, String start, String end, String token)
    {
        todayview.onShow();
        String Ntoken = "Bearer " + token;

        apiClass.updateToday(Ntoken,id,activity,start,end).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Log.e("HTTP", response.message());
                if (response.isSuccessful())
                {
                    todayview.onSuccessUpdate(response.body().getCode(), response.body().getMessage());
                    todayview.onHide();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                todayview.getError(t.getMessage());
                todayview.onHide();
                Log.e("Error",t.getMessage());
            }
        });
    }

    public void delete(String selectedId, String token)
    {
        todayview.onShow();
        String Ntoken = "Bearer " + token;

        apiClass.deleteToday(Ntoken,selectedId).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                Log.e("HTTP", response.message() + " " + response.code());
                if (response.isSuccessful())
                {
                    todayview.onSuccessDelete(response.body().getCode(), response.body().getMessage());
                    todayview.onHide();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                todayview.getError(t.getMessage());
                todayview.onHide();
                Log.e("Error",t.getMessage());
            }
        });
    }


}

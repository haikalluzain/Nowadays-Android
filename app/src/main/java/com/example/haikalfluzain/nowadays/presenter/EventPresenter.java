package com.example.haikalfluzain.nowadays.presenter;

import android.util.Log;

import com.example.haikalfluzain.nowadays.base.BasePresenter;
import com.example.haikalfluzain.nowadays.base.BaseResponse;
import com.example.haikalfluzain.nowadays.response.EventResponse;
import com.example.haikalfluzain.nowadays.view.EventView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EventPresenter<EV extends EventView> extends BasePresenter {
    EV eventview;

    public EventPresenter(EV eventview) {
        this.eventview = eventview;
    }

    public void getEvent(String token, int month, int year){
        String Ntoken = "Bearer " + token;
        eventview.onShow();
        apiClass.getEvent(Ntoken, month, year).enqueue(new Callback<EventResponse>() {
            @Override
            public void onResponse(Call<EventResponse> call, Response<EventResponse> response) {
                eventview.getHttp(Integer.toString(response.code()));
                if (response.isSuccessful()){
                    eventview.onSuccessLoadEvent(response.body().getEvents());
                    eventview.onHide();
                }
            }

            @Override
            public void onFailure(Call<EventResponse> call, Throwable t) {
                eventview.getError(t.getMessage());
                eventview.onHide();
                Log.e("Error",t.getMessage());
            }
        });

    }

    public void store(String token,String title, String desc, String start, String end, String color){
        String Ntoken = "Bearer " + token;
        eventview.onShow();
        apiClass.storeEvent(Ntoken,title,desc,start,end,color).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                eventview.getHttp(Integer.toString(response.code()));
                if (response.isSuccessful()){
                    eventview.onSuccessStoreEvent(response.body().getCode(), response.body().getMessage());
                    eventview.onHide();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                eventview.getError(t.getMessage());
                eventview.onHide();
                Log.e("Error",t.getMessage());
            }
        });
    }

    public void delete(String id,String token){
        String Ntoken = "Bearer " + token;
        eventview.onShow();
        apiClass.deleteEvent(Ntoken,id).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                eventview.getHttp(Integer.toString(response.code()));
                if (response.isSuccessful()){
                    eventview.onSuccessDeleteEvent(response.body().getCode(), response.body().getMessage());
                    eventview.onHide();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                eventview.getError(t.getMessage());
                eventview.onHide();
                Log.e("Error",t.getMessage());
            }
        });
    }

    public void update(String token,String id,String title, String desc, String start, String end, String color){
        String Ntoken = "Bearer " + token;
        eventview.onShow();
        apiClass.updateEvent(Ntoken,id,title,desc,start,end,color).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
                eventview.getHttp(Integer.toString(response.code()));
                if (response.isSuccessful()){
                    eventview.onSuccessUpdateEvent(response.body().getCode(), response.body().getMessage());
                    eventview.onHide();
                }
            }

            @Override
            public void onFailure(Call<BaseResponse> call, Throwable t) {
                eventview.getError(t.getMessage());
                eventview.onHide();
                Log.e("Error",t.getMessage());
            }
        });
    }
}

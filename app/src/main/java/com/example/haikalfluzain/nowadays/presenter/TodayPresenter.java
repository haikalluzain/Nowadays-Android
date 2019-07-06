package com.example.haikalfluzain.nowadays.presenter;

import com.example.haikalfluzain.nowadays.base.BasePresenter;
import com.example.haikalfluzain.nowadays.base.BaseResponse;
import com.example.haikalfluzain.nowadays.response.TodayResponse;
import com.example.haikalfluzain.nowadays.view.TodayView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TodayPresenter<TV extends TodayView> extends BasePresenter {
    TV todayview;

    public TodayPresenter(TV todayview){
        this.todayview = todayview;
    }

    public void getToday()
    {
        todayview.onShow();
        apiClass.getToday().enqueue(new Callback<TodayResponse>() {
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

        apiClass.storeToday(activity,start,end,Ntoken).enqueue(new Callback<BaseResponse>() {
            @Override
            public void onResponse(Call<BaseResponse> call, Response<BaseResponse> response) {
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
            }
        });
    }


}

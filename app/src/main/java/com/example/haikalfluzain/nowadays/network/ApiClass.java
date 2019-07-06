package com.example.haikalfluzain.nowadays.network;

import com.example.haikalfluzain.nowadays.base.BaseResponse;
import com.example.haikalfluzain.nowadays.response.LoginResponse;
import com.example.haikalfluzain.nowadays.response.TodayResponse;
import com.example.haikalfluzain.nowadays.response.UserResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiClass {
    @FormUrlEncoded
    @POST("user/login")
    Call<LoginResponse> login(@Field("email") String email,
                              @Field("password") String password);

    @POST("user/logout")
    Call<BaseResponse> logout(@Header("Authorization") String token);

    @POST("user/show")
    Call<UserResponse> loadAuth(@Header("Authorization") String token);

    @GET("today/show")
    Call<TodayResponse> getToday();

    @FormUrlEncoded
    @POST("today/insert")
    Call<BaseResponse> storeToday(@Field("activity") String activity,
                                  @Field("start") String start,
                                  @Field("end") String end,
                                  @Header("Authorization") String token);
}

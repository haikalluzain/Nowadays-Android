package com.example.haikalfluzain.nowadays.network;

import com.example.haikalfluzain.nowadays.base.BaseResponse;
import com.example.haikalfluzain.nowadays.response.EventResponse;
import com.example.haikalfluzain.nowadays.response.LoginResponse;
import com.example.haikalfluzain.nowadays.response.TodayResponse;
import com.example.haikalfluzain.nowadays.response.UserResponse;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiClass {

    // Auth API

    @FormUrlEncoded
    @POST("user/login")
    Call<LoginResponse> login(@Field("email") String email,
                              @Field("password") String password);
    @POST("user/logout")
    Call<BaseResponse> logout(@Header("Authorization") String token);
    @POST("user/show")
    Call<UserResponse> loadAuth(@Header("Authorization") String token);

    // Today API

    @GET("today/show")
    Call<TodayResponse> getToday(@Header("Authorization") String token);
    @FormUrlEncoded
    @POST("today/insert")
    Call<BaseResponse> storeToday(@Header("Authorization") String token,
                                  @Field("activity") String activity,
                                  @Field("start") String start,
                                  @Field("end") String end);
    @FormUrlEncoded
    @POST("today/update")
    Call<BaseResponse> updateToday(@Header("Authorization") String token,
                                   @Field("id") String id,
                                   @Field("activity") String activity,
                                   @Field("start") String start,
                                   @Field("end") String end);
    @DELETE("today/delete/{id}")
    Call<BaseResponse> deleteToday(@Header("Authorization") String token,
                                   @Path("id") String id);

    // Event API

    @FormUrlEncoded
    @POST("event/show")
    Call<EventResponse> getEvent(@Header("Authorization") String token,
                                 @Field("month") int month,
                                 @Field("year") int year);

    @FormUrlEncoded
    @POST("event/insert")
    Call<BaseResponse> storeEvent(@Header("Authorization") String token,
                                   @Field("title") String title,
                                   @Field("description") String desc,
                                   @Field("start") String start,
                                   @Field("end") String end,
                                   @Field("color") String color);

    @DELETE("event/delete/{id}")
    Call<BaseResponse> deleteEvent(@Header("Authorization") String token,
                                   @Path("id") String id);
}

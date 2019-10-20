package com.example.haikalfluzain.nowadays.network;

import android.content.Context;

import com.example.haikalfluzain.nowadays.helper.SharedPrefManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient{
    private String url;
    private Retrofit retrofit;
    private Gson gson;
    private SharedPrefManager sharedPrefManager;
    private Context context;

    public ApiClient(Context context){
        this.context = context;
        sharedPrefManager = new SharedPrefManager(context);
        url = sharedPrefManager.getIpAddress();
    }


     public Retrofit getClient()
    {
        if (retrofit == null)
        {
            gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofit;
    }

    public Retrofit getServer(String url)
    {
        if (retrofit == null)
        {
            gson = new GsonBuilder()
                    .setLenient()
                    .create();

            retrofit = new Retrofit.Builder()
                    .baseUrl(url)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }

        return retrofit;
    }
}

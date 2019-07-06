package com.example.haikalfluzain.nowadays.network;

import com.example.haikalfluzain.nowadays.config.ClientConfig;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    Retrofit retrofit;

    public Retrofit getClient()
    {
        if (retrofit == null)
        {
            retrofit = new Retrofit.Builder()
                    .baseUrl(ClientConfig.baseUrl)
                    .addConverterFactory(GsonConverterFactory.create()).build();
        }

        return retrofit;
    }
}

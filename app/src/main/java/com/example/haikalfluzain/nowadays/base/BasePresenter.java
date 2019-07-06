package com.example.haikalfluzain.nowadays.base;

import android.content.Context;

import com.example.haikalfluzain.nowadays.network.ApiClass;
import com.example.haikalfluzain.nowadays.network.ApiClient;

public class BasePresenter {
    public ApiClass apiClass = new ApiClient().getClient().create(ApiClass.class);
    public Context context;
}

package com.example.haikalfluzain.nowadays.response;

import com.example.haikalfluzain.nowadays.model.Today;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TodayResponse {
    @SerializedName("todays")
    List<Today> todays;

    public List<Today> getTodays()
    {
        return todays;
    }
}

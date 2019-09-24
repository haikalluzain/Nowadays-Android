package com.example.haikalfluzain.nowadays.base;

import com.google.gson.annotations.SerializedName;

public class BaseResponse {

    @SerializedName("code")
    String code;

    @SerializedName("message")
    String message;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}

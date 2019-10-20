package com.example.haikalfluzain.nowadays.helper;

import android.content.Context;
import android.content.SharedPreferences;

import okhttp3.Interceptor;

public class SharedPrefManager {
    public static final String SP_ID = "1";
    public static final String SP_TOKEN = "token";

    public static final String SP_LOGIN = "true";
    public static final String SP_SKIP = "true";
    public static final String IP_ADDRESS = "localhost:8000";

    SharedPreferences sp;
    SharedPreferences.Editor spEditor;

    public SharedPrefManager(Context context){
        sp = context.getSharedPreferences(SP_ID, Context.MODE_PRIVATE);
        spEditor = sp.edit();
    }

    public void saveSPString(String keySP, String value){
        spEditor.putString(keySP, value);
        spEditor.commit();
    }

    public void saveSPInt(String keySP, int value){
        spEditor.putInt(keySP, value);
        spEditor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value){
        spEditor.putBoolean(keySP, value);
        spEditor.commit();
    }

    public Integer getSpId(){
        return sp.getInt(SP_ID, 1);
    }

    public String getSpToken(){
        return sp.getString(SP_TOKEN, "");
    }

    public Boolean getSpLogin(){
        return sp.getBoolean(SP_LOGIN, false);
    }

    public Boolean getSpSkip(){
        return sp.getBoolean(SP_SKIP, false);
    }

    public String getIpAddress() {
        return sp.getString(IP_ADDRESS,"http://192.168.43.65/Laravel/nowadays/public/api/");
    }

}

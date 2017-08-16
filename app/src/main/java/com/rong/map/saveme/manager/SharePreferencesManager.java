package com.rong.map.saveme.manager;

import android.content.Context;
import android.content.SharedPreferences;
import com.rong.map.saveme.SmApplication;

/**
 * Created by Administrator on 2017/8/8/008.
 */

public class SharePreferencesManager {

    private static final String PHONE_TABLE = "phoneTable";
    private static final String CONTENT_TABLE = "contentTable";

    public static void putPhoneNum(String phoneNum){
        SharedPreferences sharedPreferences = SmApplication.Companion.getContext()
                .getSharedPreferences(PHONE_TABLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("phone", phoneNum);
        editor.apply();
    }

    public static String getPhoneNum(){
        SharedPreferences sharedPreferences = SmApplication.Companion.getContext()
                .getSharedPreferences(PHONE_TABLE, Context.MODE_PRIVATE);
        return sharedPreferences.getString("phone","");
    }

    public static void putContent(String content){
        SharedPreferences sharedPreferences = SmApplication.Companion.getContext()
                .getSharedPreferences(CONTENT_TABLE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("content", content);
        editor.apply();
    }

    public static String getContent(){
        SharedPreferences sharedPreferences = SmApplication.Companion.getContext()
                .getSharedPreferences(CONTENT_TABLE, Context.MODE_PRIVATE);
        return sharedPreferences.getString("content","");
    }

}

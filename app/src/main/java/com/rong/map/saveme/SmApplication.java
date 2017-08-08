package com.rong.map.saveme;

import android.app.Application;
import android.content.Context;

/**
 * Created by Administrator on 2017/8/8/008.
 */

public class SmApplication extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
    }
}

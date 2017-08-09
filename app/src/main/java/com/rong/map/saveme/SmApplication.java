package com.rong.map.saveme;

import android.app.Application;
import android.content.Context;
import com.blankj.utilcode.util.Utils;
import com.blankj.utilcode.util.LogUtils.Builder;

/**
 * Created by Administrator on 2017/8/8/008.
 */

public class SmApplication extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this;
        // init it in the function of onCreate in ur Application
        Utils.init(context);
        new Builder().setLogSwitch(true);
    }
}

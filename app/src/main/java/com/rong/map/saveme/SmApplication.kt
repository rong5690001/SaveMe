package com.rong.map.saveme

import android.app.Application
import android.content.Context
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.Utils
import com.blankj.utilcode.util.LogUtils.Builder

/**
 * Created by Administrator on 2017/8/8/008.
 */

class SmApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        context = this
        // init it in the function of onCreate in ur Application
        Utils.init(context)
        Builder().setLogSwitch(true)
        LogUtils.d("saveme启动")
    }

    companion object {
        lateinit var context: Context
    }
}

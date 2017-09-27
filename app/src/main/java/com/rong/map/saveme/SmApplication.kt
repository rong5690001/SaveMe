package com.rong.map.saveme

import android.app.Activity
import android.app.Application
import android.content.Context
import android.os.Bundle
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.LogUtils.Builder
import com.blankj.utilcode.util.Utils

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

        registerActivityLifecycleCallbacks(object:ActivityLifecycleCallbacks{
            override fun onActivityStarted(p0: Activity?) {
            }

            override fun onActivityDestroyed(p0: Activity?) {
            }

            override fun onActivitySaveInstanceState(p0: Activity?, p1: Bundle?) {
            }

            override fun onActivityStopped(p0: Activity?) {
            }

            override fun onActivityCreated(p0: Activity?, p1: Bundle?) {
            }

            override fun onActivityPaused(p0: Activity?) {
            }

            override fun onActivityResumed(p0: Activity?) {
                LogUtils.d("activity->onResumed:", p0!!.localClassName)
            }

        })
    }

    companion object {
        lateinit var context: Context
        lateinit var location: String
    }
}

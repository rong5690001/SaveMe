package com.rong.map.saveme.service

import android.app.KeyguardManager
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.view.Gravity
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.WindowManager
import android.widget.RelativeLayout

import com.blankj.utilcode.util.LogUtils
import com.rong.map.saveme.R
import com.rong.map.saveme.activity.LockActivity
import com.rong.map.saveme.activity.SetPsdActivity

/**
 * 作者：陈华榕
 * 邮箱:mpa.chen@sportq.com
 * 时间：2017/8/9  18:24
 */

class LockService : Service() {

    private var lockIntent: Intent? = null
    private var mKeyguardManager: KeyguardManager? = null
    private var mKeyguardLock: KeyguardManager.KeyguardLock? = null

    override fun onCreate() {
        super.onCreate()

        lockIntent = Intent(this, LockActivity::class.java)
        lockIntent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        /* 注册广播 */
        val mScreenOnFilter = IntentFilter(Intent.ACTION_SCREEN_ON)
        this@LockService.registerReceiver(mScreenOnReceiver, mScreenOnFilter)

        /* 注册广播 */
        val mScreenOffFilter = IntentFilter(Intent.ACTION_SCREEN_OFF)
        this@LockService.registerReceiver(mScreenOffReceiver, mScreenOffFilter)

        //        createView();
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mScreenOnReceiver)
        unregisterReceiver(mScreenOffReceiver)
        startService(Intent(this, LockService::class.java))
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    // 屏幕变亮的广播,我们要隐藏默认的锁屏界面
    private val mScreenOnReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == Intent.ACTION_SCREEN_ON) {
                LogUtils.i(TAG, "----------------- Intent.ACTION_SCREEN_ON------")
            }
        }
    }

    // 屏幕变暗/变亮的广播 ， 我们要调用KeyguardManager类相应方法去解除屏幕锁定
    private val mScreenOffReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == Intent.ACTION_SCREEN_OFF || action == Intent.ACTION_SCREEN_ON) {
                mKeyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
                mKeyguardLock = mKeyguardManager!!.newKeyguardLock("")
                mKeyguardLock!!.disableKeyguard()
                startActivity(lockIntent)
            }
        }
    }

    companion object {

        private val TAG = "LockService"
    }
}

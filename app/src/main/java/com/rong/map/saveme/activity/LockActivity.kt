package com.rong.map.saveme.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import com.rong.map.saveme.R
import com.rong.map.saveme.base.BaseActivity
import com.rong.map.saveme.event.SendMsgEvent
import com.rong.map.saveme.manager.SMSManager
import com.rong.map.saveme.utils.LockscreenUtils
import com.rong.map.saveme.widget.LockView
import org.greenrobot.eventbus.EventBus

class LockActivity : BaseActivity(), LockscreenUtils.OnLockStatusChangedListener {

    internal lateinit var windowManager: WindowManager
    internal lateinit var contentLayout: LinearLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
        setContentView(R.layout.activity_lock2)

        //创建window
        createWindow()
    }

    override fun onLockStatusChanged(isLocked: Boolean) {
        if (!isLocked) {
            finish()
        }
    }

    /**
     * 创建window
     */
    @SuppressLint("WrongConstant")
    private fun createWindow() {
        val windowParams = WindowManager.LayoutParams()
        windowManager = application
                .getSystemService("window") as WindowManager

        windowParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
        windowParams.gravity = Gravity.LEFT or Gravity.TOP

        windowParams.x = 0
        windowParams.y = 0

        windowParams.width = WindowManager.LayoutParams.MATCH_PARENT
        windowParams.height = WindowManager.LayoutParams.MATCH_PARENT

        val inflater = LayoutInflater.from(this)

        val contentView = inflater.inflate(R.layout.activity_lock2, null)

        contentLayout = contentView.findViewById<View>(R.id.contentLayout) as LinearLayout
        var lockView = contentView.findViewById<View>(R.id.mLockView) as LockView
        windowManager.addView(contentLayout, windowParams)

        contentLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.AT_MOST))

        lockView.setOnLockListener(object : LockView.OnLockListener {

            override fun onSucceed(result: String) {
                if (SMSManager.checkPsd(result)) {
                    windowManager.removeView(contentLayout)
                    finish()
                } else if (SMSManager.checkSMPsd(result)) {
                    EventBus.getDefault().post(SendMsgEvent())
                }
            }

            override fun onError() {

            }

        })

    }

}

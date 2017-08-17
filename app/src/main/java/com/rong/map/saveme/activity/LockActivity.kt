package com.rong.map.saveme.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.LinearLayout
import com.blankj.utilcode.util.SPUtils
import com.rong.map.saveme.R
import com.rong.map.saveme.base.BaseActivity
import com.rong.map.saveme.event.SendMsgEvent
import com.rong.map.saveme.utils.CstUtils
import com.rong.map.saveme.widget.LockView
import org.greenrobot.eventbus.EventBus

class LockActivity : BaseActivity() {

    internal lateinit var windowManager: WindowManager
    internal lateinit var contentLayout: LinearLayout
    internal lateinit var lockView: LockView

    override fun onCreate(savedInstanceState: Bundle?) {
//        window.setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG)
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_lock2)
        createView()

    }

//    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
//        if(keyCode == KeyEvent.KEYCODE_HOME
//                || keyCode == KeyEvent.KEYCODE_BACK){
//            return true
//        }
//        return super.onKeyDown(keyCode, event)
//    }
//
//    override fun onAttachedToWindow() {
////        window.setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG)
//        super.onAttachedToWindow()
//    }

    @SuppressLint("WrongConstant")
    fun createView() {

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
        lockView = contentView.findViewById<View>(R.id.lockView) as LockView
        windowManager.addView(contentLayout, windowParams)
//        contentLayout.setOnTouchListener(View.OnTouchListener { view, motionEvent ->
//
//            false
//        })

        contentLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.AT_MOST))

        lockView.setOnLockListener(object : LockView.OnLockListener {

            override fun onSucceed(result: String) {
                if (result == SPUtils.getInstance(CstUtils.TABLENAME)
                        .getString(CstUtils.KEY_PASSWORD)) {
                    windowManager.removeView(contentLayout)
                    finish()
                } else if (result == SPUtils.getInstance(CstUtils.TABLENAME)
                        .getString(CstUtils.KEY_PSDSAVEME)) {
                    EventBus.getDefault().post(SendMsgEvent())
                }
            }

            override fun onError() {

            }

        })

    }

}

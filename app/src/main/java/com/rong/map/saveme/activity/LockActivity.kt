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
import com.rong.map.saveme.utils.LockscreenUtils
import com.rong.map.saveme.widget.LockView
import kotlinx.android.synthetic.main.activity_lock2.*
import org.greenrobot.eventbus.EventBus

class LockActivity : BaseActivity(), LockscreenUtils.OnLockStatusChangedListener{

    internal lateinit var windowManager: WindowManager
    internal lateinit var contentLayout: LinearLayout
//    internal var mLockscreenUtils = LockscreenUtils()

    override fun onCreate(savedInstanceState: Bundle?) {
//        window.setType(WindowManager.LayoutParams.TYPE_KEYGUARD_DIALOG)
        super.onCreate(savedInstanceState)
        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        window.addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        window.addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD)
        setContentView(R.layout.activity_lock2)
//        mLockscreenUtils.lock(this)
//        initView()
        createView()

    }

    private fun initView(){
        lockView.setOnLockListener(object : LockView.OnLockListener {

            override fun onSucceed(result: String) {
                if (result == SPUtils.getInstance(CstUtils.TABLENAME)
                        .getString(CstUtils.KEY_PASSWORD)) {
                    windowManager.removeView(contentLayout)
//                    mLockscreenUtils.unlock()
                } else if (result == SPUtils.getInstance(CstUtils.TABLENAME)
                        .getString(CstUtils.KEY_PSDSAVEME)) {
                    EventBus.getDefault().post(SendMsgEvent())
                }
            }

            override fun onError() {

            }

        })
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

    override fun onLockStatusChanged(isLocked: Boolean) {
        if(!isLocked){
            finish()
        }
    }


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
        var lockView = contentView.findViewById<View>(R.id.lockView) as LockView
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
//                    mLockscreenUtils.unlock()
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

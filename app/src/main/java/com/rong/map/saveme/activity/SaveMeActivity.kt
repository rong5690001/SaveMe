package com.rong.map.saveme.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.MainThread
import android.view.MotionEvent
import android.view.View
import android.view.animation.RotateAnimation
import com.rong.map.saveme.R
import com.rong.map.saveme.event.SendMsgEvent
import com.rong.map.saveme.event.SendMsgResultEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_save_me.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.concurrent.TimeUnit

class SaveMeActivity : AppCompatActivity() {

    var subscription: Disposable? = null
    var sendBtnTouchObser = Observable.timer(10, TimeUnit.SECONDS)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_me)
        EventBus.getDefault().register(this)
        sendBtn.setOnTouchListener { view, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    subscription = sendBtnTouchObser.subscribe({
                        startActivity(Intent(this@SaveMeActivity
                                , SettingsActivity::class.java))
                    })
                }

                MotionEvent.ACTION_UP -> {
                    if (subscription!!.isDisposed) {
                        subscription!!.dispose()
                    }
                }
            }
            false
        }
        sendBtn.setOnClickListener {
            sendBtn.isClickable = false
            var rotation = RotateAnimation(1f, 1.5f)
            sendBtn.startAnimation(rotation)

            EventBus.getDefault().post(SendMsgEvent())
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun sendResult(event: SendMsgResultEvent) {
        sendBtn.isClickable = true
        if (event.isOk) {
            sendBtn.setBackgroundResource(R.drawable.circle_btn_bg_succed)
        } else {
            sendBtn.setBackgroundResource(R.drawable.circle_btn_bg_fail)
            Observable.timer(2, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        sendBtn.setBackgroundResource(R.drawable.circle_btn_bg_normal)
                    }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}

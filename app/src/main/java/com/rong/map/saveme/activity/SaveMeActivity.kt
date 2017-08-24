package com.rong.map.saveme.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.RotateAnimation
import com.rong.map.saveme.R
import com.rong.map.saveme.event.SendMsgEvent
import kotlinx.android.synthetic.main.activity_save_me.*
import org.greenrobot.eventbus.EventBus

class SaveMeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_me)
        sendBtn.setOnClickListener {
            var rotation = RotateAnimation(1f, 1.5f)
            sendBtn.startAnimation(rotation)

            EventBus.getDefault().post(SendMsgEvent())
        }
    }
}

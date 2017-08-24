package com.rong.map.saveme.activity

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.animation.RotateAnimation
import com.rong.map.saveme.R
import kotlinx.android.synthetic.main.activity_save_me.*

class SaveMeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_me)
        sendBtn.setOnClickListener {
            var rotation = RotateAnimation(1f, 1.5f)
            sendBtn.startAnimation(rotation)
        }
    }
}

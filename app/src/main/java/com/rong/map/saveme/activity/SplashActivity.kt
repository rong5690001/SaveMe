package com.rong.map.saveme.activity

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.rong.map.saveme.R
import com.rong.map.saveme.service.LockService

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        startService(Intent(this, LockService::class.java))
        Handler().postDelayed({
            startActivity(Intent(this@SplashActivity
                    , SettingsActivity::class.java))
            finish()
        }, 2000)
    }
}

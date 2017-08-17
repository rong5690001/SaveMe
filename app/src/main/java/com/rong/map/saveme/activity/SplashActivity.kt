package com.rong.map.saveme.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.rong.map.saveme.R
import com.rong.map.saveme.base.BaseActivity
import com.rong.map.saveme.service.LockService
import com.rong.map.saveme.service.LockscreenService

class SplashActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        startService(Intent(this, LockService::class.java))
        Handler().postDelayed({
            var intent = Intent(this@SplashActivity
                    , SettingsActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
            startActivity(intent)
            finish()
        }, 2000)
    }
}

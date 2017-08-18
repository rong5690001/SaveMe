package com.rong.map.saveme.activity

import android.content.Intent
import android.os.Bundle
import com.rong.map.saveme.R
import com.rong.map.saveme.base.BaseActivity
import com.rong.map.saveme.widget.LockView
import kotlinx.android.synthetic.main.activity_lock2.*

class Lock2Activity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lock2)
        mLockView.setOnLockListener(object : LockView.OnLockListener {

            override fun onError() {

            }

            override fun onSucceed(result: String) {
                startActivity(Intent(this@Lock2Activity
                        , SettingsActivity::class.java))
            }

        })
    }
}

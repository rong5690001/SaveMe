package com.rong.map.saveme.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.StringUtils
import com.rong.map.saveme.R
import com.rong.map.saveme.base.BaseActivity
import com.rong.map.saveme.service.LockService
import com.rong.map.saveme.utils.CstUtils

class SplashActivity : BaseActivity() {

    private lateinit var mIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        startService(Intent(this@SplashActivity, LockService::class.java))
        mIntent = Intent(this@SplashActivity
                , SettingsActivity::class.java)
        var psd = SPUtils.getInstance(CstUtils.TABLE_PASSWORD)
                .getString(CstUtils.KEY_PASSWORD);
        var smPsd = SPUtils.getInstance(CstUtils.TABLE_PASSWORD)
                .getString(CstUtils.KEY_PSDSAVEME);
        if (!(StringUtils.isEmpty(psd)
                && StringUtils.isEmpty(smPsd))) {//已经设置密码
            mIntent.setClass(this@SplashActivity
                    , Lock2Activity::class.java)
        }
        Handler().postDelayed({
            startActivity(mIntent)
            finish()
        }, 2000)
    }
}

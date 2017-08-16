package com.rong.map.saveme.activity

import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import com.blankj.utilcode.util.SPUtils
import com.rong.map.saveme.base.BaseActivity
import com.rong.map.saveme.R
import com.rong.map.saveme.utils.CstUtils
import com.rong.map.saveme.widget.LockView

class SetPsdActivity : BaseActivity() {

    companion object {
        val KEY_TYPE = "key.type"
    }

    internal var mTitle: TextView? = null
    internal var mLockView: LockView? = null
    internal var confirm = 2
    internal var mResults = ""
    internal var isSaveMe = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lock)
        isSaveMe = intent.getBooleanExtra(KEY_TYPE, false)
        initView()

        //        startService(intent);
    }

    fun showTitleError() {
        confirm = 2
        mTitle!!.setTextColor(Color.RED)
        mTitle!!.setText(R.string.lckViewErrorMs)
        Handler().postDelayed({
            mTitle!!.setText(R.string.resetyourpassword)
        }, 1000)
    }

    /**
     * 初始化view
     */
    fun initView() {
        mTitle = findViewById(R.id.title);
        mLockView = findViewById(R.id.lockView);
        mLockView!!.setSetPsd(true)
        mLockView!!.setOnLockListener(object : LockView.OnLockListener {
            override fun onSucceed(results: String) {
                confirm--
                mTitle!!.setTextColor(Color.parseColor("#3b3b3b"))
                if (getStringRes(R.string.setyourpassword) == mTitle!!.text.toString()) {
                    mTitle!!.setText(R.string.confirmpassword)
                }

                if (confirm == 0) {//再次确认成功
                    if (mResults.equals(results)) {
                        mTitle!!.setText(R.string.setpswscd)
                        var key = if (isSaveMe) CstUtils.KEY_PSDSAVEME else CstUtils.KEY_PASSWORD
                        SPUtils.getInstance(CstUtils.TABLENAME).put(key, results)
                        Handler().postDelayed({
                            finish()
                        }, 1000)
                    } else {
                        showTitleError()
                    }
                }

                mResults = results
            }

            override fun onError() {
                showTitleError()
            }
        })
    }

    override fun onDestroy() {
        //        stopService(intent);
        super.onDestroy()
    }
}

package com.rong.map.saveme.activity

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.TextView

import com.blankj.utilcode.util.SPUtils
import com.rong.map.saveme.base.BaseActivity
import com.rong.map.saveme.R
import com.rong.map.saveme.service.LockService
import com.rong.map.saveme.utils.CstUtils
import com.rong.map.saveme.widget.LockView

import butterknife.BindView
import butterknife.ButterKnife

class SetPsdActivity : BaseActivity() {
    @BindView(R.id.title)
    internal var mTitle: TextView? = null
    @BindView(R.id.lockView)
    internal var mLockView: LockView? = null
    private var confirm = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lock)
        ButterKnife.bind(this)
//        var intent = Intent(this, LockService::class.java)
        mLockView!!.setSetPsd(true)
        mLockView!!.setOnLockListener(object : LockView.OnLockListener {
            override fun onSucceed(results: String) {
                confirm--
                if (confirm == 0) {//再次确认成功
                    SPUtils.getInstance(CstUtils.TABLENAME).put(CstUtils.KEY_PASSWORD, results)
                }
                mTitle!!.setTextColor(Color.parseColor("#3b3b3b"))
                if (getStringRes(R.string.setyourpassword) == mTitle!!.text.toString()) {
                    mTitle!!.setText(R.string.confirmpassword)
                } else if (getStringRes(R.string.confirmpassword) == mTitle!!.text.toString()) {
                    mTitle!!.setText(R.string.setpswscd)
                }
            }

            override fun onError() {
                confirm = 2
                mTitle!!.setTextColor(Color.RED)
                mTitle!!.setText(R.string.lckViewErrorMs)
            }
        })
        //        startService(intent);
    }

    override fun onDestroy() {
        //        stopService(intent);
        super.onDestroy()
    }
}

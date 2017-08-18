package com.rong.map.saveme.activity

import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.telephony.SmsManager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.blankj.utilcode.util.RegexUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.ToastUtils
import com.google.gson.Gson
import com.rong.map.saveme.R
import com.rong.map.saveme.SmApplication
import com.rong.map.saveme.base.BaseActivity
import com.rong.map.saveme.manager.SPManager
import com.rong.map.saveme.model.MsgData
import com.rong.map.saveme.service.LockService
import com.rong.map.saveme.utils.CstUtils
import kotlinx.android.synthetic.main.activity_main.*

class SetMsgActivity : BaseActivity(), View.OnClickListener {

    /**
     * 保存
     */
    private var mSaveBtn: Button? = null
    private var sentPI: PendingIntent? = null
    private var deliverPI: PendingIntent? = null
    private var msgData: MsgData = SPManager.msgData ?: MsgData("", "")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
    }

    private fun initView() {
        phone!!.setText(msgData.phoneNum)
        phone!!.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable?) {
                msgData.phoneNum = p0.toString();
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

        })
        content!!.setText(msgData.msg)
        content!!.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                msgData.msg = p0.toString();
            }

        })
        mSaveBtn = findViewById<View>(R.id.saveBtn) as Button
        mSaveBtn!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.saveBtn -> {
                if (canSave()) {
//                    sendSMS()
                    SPManager.putMsgData(msgData)
                    ToastUtils.showShort(getString(R.string.saved))
                    finish()
                } else {
                    ToastUtils.showShort(getString(R.string.inputTip))
                }
            }
        }
    }

    private fun canSave(): Boolean {
        return !TextUtils.isEmpty(msgData.msg)
                && !TextUtils.isEmpty(msgData.phoneNum)
                && RegexUtils.isMobileExact(msgData.phoneNum)
    }
}

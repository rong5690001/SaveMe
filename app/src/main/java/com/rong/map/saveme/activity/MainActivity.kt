package com.rong.map.saveme.activity

import android.app.Activity
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.telephony.SmsManager
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.rong.map.saveme.R
import com.rong.map.saveme.base.BaseActivity
import com.rong.map.saveme.manager.SharePreferencesManager
import com.rong.map.saveme.service.LockService

class MainActivity : BaseActivity(), View.OnClickListener {

    /**
     * 紧急联系人电话
     */
    private var mPhone: EditText? = null
    /**
     * 短信内容
     */
    private var mContent: EditText? = null
    /**
     * 保存
     */
    private var mSaveBtn: Button? = null
    private var sentPI: PendingIntent? = null
    private var deliverPI: PendingIntent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initView()
        initSMS()
        startService(Intent(this, LockService::class.java))
    }

    private fun initView() {
        mPhone = findViewById<View>(R.id.phone) as EditText
        mPhone!!.setText(SharePreferencesManager.getPhoneNum())
        mContent = findViewById<View>(R.id.content) as EditText
        mContent!!.setText(SharePreferencesManager.getContent())
        mSaveBtn = findViewById<View>(R.id.saveBtn) as Button
        mSaveBtn!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.saveBtn -> {
                if (canSave()) {
                    sendSMS(mPhone!!.text.toString(), mContent!!.text.toString())
                } else {
                    Toast.makeText(this@MainActivity,
                            "请输入完成内容", Toast.LENGTH_SHORT)
                            .show()
                }
                //TODO
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun initSMS() {
        //处理返回的发送状态
        val SENT_SMS_ACTION = "SENT_SMS_ACTION"
        val sentIntent = Intent(SENT_SMS_ACTION)
        sentPI = PendingIntent.getBroadcast(this, 0, sentIntent,
                0)
        // register the Broadcast Receivers
        this.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(_context: Context, _intent: Intent) {
                when (resultCode) {
                    Activity.RESULT_OK -> {
                        SharePreferencesManager.putPhoneNum(mPhone!!.text.toString())
                        Toast.makeText(this@MainActivity,
                                "短信发送成功", Toast.LENGTH_SHORT)
                                .show()
                    }
                    SmsManager.RESULT_ERROR_GENERIC_FAILURE -> {
                    }
                    SmsManager.RESULT_ERROR_RADIO_OFF -> {
                    }
                    SmsManager.RESULT_ERROR_NULL_PDU -> {
                    }
                }
            }
        }, IntentFilter(SENT_SMS_ACTION))

        //处理返回的接收状态
        val DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION"
        // create the deilverIntent parameter
        val deliverIntent = Intent(DELIVERED_SMS_ACTION)
        deliverPI = PendingIntent.getBroadcast(this, 0,
                deliverIntent, 0)
        this.registerReceiver(object : BroadcastReceiver() {
            override fun onReceive(_context: Context, _intent: Intent) {
                Toast.makeText(this@MainActivity,
                        "收信人已经成功接收", Toast.LENGTH_SHORT)
                        .show()
            }
        }, IntentFilter(DELIVERED_SMS_ACTION))
    }

    /**
     * 直接调用短信接口发短信
     *
     * @param phoneNumber
     * @param message
     */
    fun sendSMS(phoneNumber: String, message: String) {
        SharePreferencesManager.putContent(mContent!!.text.toString())
        //获取短信管理器
        val smsManager = android.telephony.SmsManager.getDefault()
        //拆分短信内容（手机短信长度限制）
        val divideContents = smsManager.divideMessage(message)
        for (text in divideContents) {
            smsManager.sendTextMessage(phoneNumber, null, text, sentPI, deliverPI)
        }
    }

    private fun canSave(): Boolean {
        if (TextUtils.isEmpty(mPhone!!.text.toString())) return false
        return if (TextUtils.isEmpty(mContent!!.text.toString())) false else true
    }
}

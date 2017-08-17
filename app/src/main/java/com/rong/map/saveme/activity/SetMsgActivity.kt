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
import com.google.gson.Gson
import com.rong.map.saveme.R
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
        initSMS()
        startService(Intent(this, LockService::class.java))
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
                msgData.msg = p0.toString();
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
            }

        })
        mSaveBtn = findViewById<View>(R.id.saveBtn) as Button
        mSaveBtn!!.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.saveBtn -> {
                if (canSave()) {
                    sendSMS(phone!!.text.toString(), content!!.text.toString())
                    SPUtils.getInstance(CstUtils.TABLE_MSG)
                            .put(CstUtils.KEY_MSG
                                    , Gson().toJson(msgData))
                } else {
                    Toast.makeText(this@SetMsgActivity,
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
                        SPManager.putPhoneNum(phone!!.text.toString())
                        Toast.makeText(this@SetMsgActivity,
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
                Toast.makeText(this@SetMsgActivity,
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
        SPManager.putContent(content!!.text.toString())
        //获取短信管理器
        val smsManager = android.telephony.SmsManager.getDefault()
        //拆分短信内容（手机短信长度限制）
        val divideContents = smsManager.divideMessage(message)
        for (text in divideContents) {
            smsManager.sendTextMessage(phoneNumber, null, text, sentPI, deliverPI)
        }
    }

    private fun canSave(): Boolean {
        return !TextUtils.isEmpty(msgData.msg)
                && !TextUtils.isEmpty(msgData.phoneNum)
                && RegexUtils.isMobileExact(msgData.phoneNum)
    }
}

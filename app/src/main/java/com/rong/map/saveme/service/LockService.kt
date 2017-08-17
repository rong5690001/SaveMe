package com.rong.map.saveme.service

import android.app.Activity
import android.app.KeyguardManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.telephony.SmsManager
import android.widget.Toast
import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SPUtils
import com.rong.map.saveme.SmApplication
import com.rong.map.saveme.activity.LockActivity
import com.rong.map.saveme.event.SendMsgEvent
import com.rong.map.saveme.manager.SPManager
import com.rong.map.saveme.model.MsgData
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


/**
 * 作者：陈华榕
 * 邮箱:mpa.chen@sportq.com
 * 时间：2017/8/9  18:24
 */

class LockService : Service() {

    private var lockIntent: Intent? = null
    private var mKeyguardManager: KeyguardManager? = null
    private var mKeyguardLock: KeyguardManager.KeyguardLock? = null
    private var sentPI: PendingIntent? = null
    private var deliverPI: PendingIntent? = null

    override fun onCreate() {
        super.onCreate()
        EventBus.getDefault().register(this@LockService)
        lockIntent = Intent(this, LockActivity::class.java)
        lockIntent!!.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

        /* 注册广播 */
        val mScreenOnFilter = IntentFilter(Intent.ACTION_SCREEN_ON)
        this@LockService.registerReceiver(mScreenOnReceiver, mScreenOnFilter)

        /* 注册广播 */
        val mScreenOffFilter = IntentFilter(Intent.ACTION_SCREEN_OFF)
        this@LockService.registerReceiver(mScreenOffReceiver, mScreenOffFilter)

        initSMS()
        //        createView();
    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(mScreenOnReceiver)
        unregisterReceiver(mScreenOffReceiver)
        startService(Intent(this, LockService::class.java))
    }

    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onMessageEvent(event: SendMsgEvent) {
        sendSMS(SPManager.msgData!!)
    };

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
//                        Toast.makeText(SmApplication.context,
//                                "短信发送成功", Toast.LENGTH_SHORT)
//                                .show()
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
//                Toast.makeText(SmApplication.context,
//                        "收信人已经成功接收", Toast.LENGTH_SHORT)
//                        .show()
            }
        }, IntentFilter(DELIVERED_SMS_ACTION))
    }

    /**
     * 直接调用短信接口发短信
     *
     * @param msgData
     */
    fun sendSMS(msgData: MsgData) {
        //获取短信管理器
        val smsManager = android.telephony.SmsManager.getDefault()
        //拆分短信内容（手机短信长度限制）
        val divideContents = smsManager.divideMessage(msgData.msg)
        for (text in divideContents) {
            smsManager.sendTextMessage(msgData.phoneNum, null, text, sentPI, deliverPI)
        }
    }

    // 屏幕变亮的广播,我们要隐藏默认的锁屏界面
    private val mScreenOnReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            if (intent.action == Intent.ACTION_SCREEN_ON) {
                LogUtils.i(TAG, "----------------- Intent.ACTION_SCREEN_ON------")
            }
        }
    }

    // 屏幕变暗/变亮的广播 ， 我们要调用KeyguardManager类相应方法去解除屏幕锁定
    private val mScreenOffReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            if (action == Intent.ACTION_SCREEN_OFF || action == Intent.ACTION_SCREEN_ON) {
                mKeyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
                mKeyguardLock = mKeyguardManager!!.newKeyguardLock("")
                mKeyguardLock!!.disableKeyguard()
                startActivity(lockIntent)
            }
        }
    }

    companion object {
        private val TAG = "LockService"
    }
}

package com.rong.map.saveme.BroadcastReceiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.blankj.utilcode.util.LogUtils
import com.rong.map.saveme.service.LockService

/**
 * 作者：陈华榕
 * 邮箱:mpa.chen@sportq.com
 * 时间：2017/8/17  15:21
 */

class AutoLanuchReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        LogUtils.d("接收到广播")
        if (intent.action == "android.media.AUDIO_BECOMING_NOISY") {
            LogUtils.d("接收到广播:android.media.AUDIO_BECOMING_NOISY")
            context.startActivity(Intent(context, LockService::class.java))
        }
    }
}

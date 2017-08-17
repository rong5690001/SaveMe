package com.rong.map.saveme.base

import android.app.Service
import android.content.Intent
import android.os.IBinder

/**
 * 作者：陈华榕
 * 邮箱:mpa.chen@sportq.com
 * 时间：2017/8/17  18:20
 */
abstract class BaseService : Service() {

    override fun startActivity(intent: Intent?) {
        intent!!.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS)
        super.startActivity(intent)
    }

}
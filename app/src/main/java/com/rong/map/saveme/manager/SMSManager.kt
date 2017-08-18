package com.rong.map.saveme.manager

import com.blankj.utilcode.util.SPUtils
import com.rong.map.saveme.event.SendMsgEvent
import com.rong.map.saveme.utils.CstUtils
import org.greenrobot.eventbus.EventBus

/**
 * Created by Administrator on 2017/8/8/008.
 */

object SMSManager {

    fun checkPsd(result: String): Boolean {
        return result == SPUtils.getInstance(CstUtils.TABLE_PASSWORD)
                .getString(CstUtils.KEY_PASSWORD)
    }

    fun checkSMPsd(result: String): Boolean {
        return result == SPUtils.getInstance(CstUtils.TABLE_PASSWORD)
                .getString(CstUtils.KEY_PSDSAVEME)
    }

}

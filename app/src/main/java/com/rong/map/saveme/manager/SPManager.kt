package com.rong.map.saveme.manager

import com.blankj.utilcode.util.LogUtils
import com.blankj.utilcode.util.SPUtils
import com.blankj.utilcode.util.StringUtils
import com.google.gson.Gson
import com.rong.map.saveme.model.MsgData
import com.rong.map.saveme.utils.CstUtils

/**
 * Created by Administrator on 2017/8/8/008.
 */

object SPManager {

    /**
     * 获取信息
     */
    val msgData: MsgData?
        get() {
            val msg = SPUtils.getInstance(CstUtils.TABLE_MSG).getString(CstUtils.KEY_MSG, "")
            if(StringUtils.isEmpty(msg)){
                return null
            }
            return Gson().fromJson(msg, MsgData::class.java)
        }

    /**
     * 存储信息
     */
    fun putMsgData(msgData: MsgData) {
        try {
            SPUtils.getInstance(CstUtils.TABLE_MSG)
                    .put(CstUtils.KEY_MSG, Gson().toJson(msgData))
        } catch (e: Exception) {
            LogUtils.e(e)
        }
    }

}

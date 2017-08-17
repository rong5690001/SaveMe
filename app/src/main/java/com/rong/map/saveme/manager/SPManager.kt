package com.rong.map.saveme.manager

import android.content.Context
import android.content.SharedPreferences

import com.blankj.utilcode.util.SPUtils
import com.google.gson.Gson
import com.rong.map.saveme.SmApplication
import com.rong.map.saveme.model.MsgData
import com.rong.map.saveme.utils.CstUtils

/**
 * Created by Administrator on 2017/8/8/008.
 */

object SPManager {

    private val PHONE_TABLE = "phoneTable"
    private val CONTENT_TABLE = "contentTable"

    fun putPhoneNum(phoneNum: String) {
        val sharedPreferences = SmApplication.context
                .getSharedPreferences(PHONE_TABLE, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("phone", phoneNum)
        editor.apply()
    }

    val msgData: MsgData?
        get() {
            val msg = SPUtils.getInstance(CstUtils.TABLE_MSG).getString(CstUtils.KEY_MSG)
            return Gson().fromJson(msg, MsgData::class.java)
        }

    fun putContent(content: String) {
        val sharedPreferences = SmApplication.context
                .getSharedPreferences(CONTENT_TABLE, Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.putString("content", content)
        editor.apply()
    }

    val content: String
        get() {
            val sharedPreferences = SmApplication.context
                    .getSharedPreferences(CONTENT_TABLE, Context.MODE_PRIVATE)
            return sharedPreferences.getString("content", "")
        }

}

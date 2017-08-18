package com.rong.map.saveme.base

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    protected fun getStringRes(resId: Int): String {
        return resources.getString(resId)
    }
}

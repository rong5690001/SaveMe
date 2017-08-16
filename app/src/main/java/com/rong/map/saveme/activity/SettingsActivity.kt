package com.rong.map.saveme.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import com.rong.map.saveme.R
import com.rong.map.saveme.base.BaseActivity

class SettingsActivity : BaseActivity(), View.OnClickListener {
    internal var setLockPattern: TextView? = null

    internal var setSaveMePattern: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setLockPattern = findViewById(R.id.setLockPattern)
        setLockPattern!!.setOnClickListener(this)
        setSaveMePattern = findViewById(R.id.setSaveMePattern)
        setSaveMePattern!!.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {
        if (p0 != null) {
            when (p0.id) {
                R.id.setLockPattern -> {
                    var intent = Intent(this, SetPsdActivity::class.java)
                    intent.putExtra(SetPsdActivity.KEY_TYPE, false)
                    startActivity(intent)
                }

                R.id.setSaveMePattern -> {
                    var intent = Intent(this, SetPsdActivity::class.java)
                    intent.putExtra(SetPsdActivity.KEY_TYPE, true)
                    startActivity(intent)
                }


            }
        }
    }


}

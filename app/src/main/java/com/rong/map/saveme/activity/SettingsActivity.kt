package com.rong.map.saveme.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.rong.map.saveme.R
import com.rong.map.saveme.base.BaseActivity
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : BaseActivity(), View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        setLockPattern!!.setOnClickListener(this)
        setSaveMePattern!!.setOnClickListener(this)
        setMsg!!.setOnClickListener(this)

//        Thread(Runnable {
//            object : AsyncTask<String, Integer, String>() {
//                override fun doInBackground(vararg p0: String?): String {
//                    return "21"
//                }
//
//
//            }.execute()
//        }).start()
        //TODO Test

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

                R.id.setMsg -> {
                    var intent = Intent(this, SetMsgActivity::class.java)
                    startActivity(intent)
                }

            }
        }
    }


}

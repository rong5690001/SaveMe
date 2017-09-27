package com.rong.map.saveme.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.annotation.MainThread
import android.view.MotionEvent
import android.view.View
import android.view.animation.*
import com.rong.map.saveme.R
import com.rong.map.saveme.event.SendMsgEvent
import com.rong.map.saveme.event.SendMsgResultEvent
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.activity_save_me.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.concurrent.TimeUnit
import com.rong.map.saveme.listener.MyLocationListener
import com.baidu.location.BDAbstractLocationListener
import com.baidu.location.LocationClient
import com.baidu.location.LocationClientOption.LocationMode
import com.baidu.location.LocationClientOption


class SaveMeActivity : AppCompatActivity() {

    var subscription: Disposable? = null
    var sendBtnTouchObser = Observable.timer(10, TimeUnit.SECONDS)

    var mLocationClient: LocationClient? = null

    var myListener: BDAbstractLocationListener = MyLocationListener()
//BDAbstractLocationListener为7.2版本新增的Abstract类型的监听接口，原有BDLocationListener接口暂时同步保留。具体介绍请参考后文中的说明

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_save_me)
        EventBus.getDefault().register(this)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                // 申请一个（或多个）权限，并提供用于回调返回的获取码（用户定义)
                requestPermissions(arrayOf(
                        Manifest.permission.READ_PHONE_STATE
                        , Manifest.permission.READ_PHONE_STATE
                        , Manifest.permission.ACCESS_COARSE_LOCATION
                        , Manifest.permission.ACCESS_FINE_LOCATION
                        , Manifest.permission.READ_EXTERNAL_STORAGE
                        , Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.SEND_SMS
                ), 100)
            }
        }
//        sendBtn.setOnTouchListener { view, event ->
//            when (event.action) {
//                MotionEvent.ACTION_DOWN -> {
//                    subscription = sendBtnTouchObser.subscribe({
//                        startActivity(Intent(this@SaveMeActivity
//                                , SettingsActivity::class.java))
//                    })
//                }
//
//                MotionEvent.ACTION_UP -> {
//                    if (subscription!!.isDisposed) {
//                        subscription!!.dispose()
//                    }
//                }
//            }
//            false
//        }
        sendBtn.setOnClickListener {
            //            sendBtn.isClickable = false
            var rotation = RotateAnimation(1f, 1.5f)
            sendBtn.startAnimation(rotation)

            EventBus.getDefault().post(SendMsgEvent())
        }

        mLocationClient = LocationClient(getApplicationContext());
        initLocation();
        //声明LocationClient类
        mLocationClient!!.registerLocationListener(myListener);
        //注册监听函数
        mLocationClient!!.start()
    }

    private fun initLocation() {

        val option = LocationClientOption()
        option.locationMode = LocationMode.Hight_Accuracy
        //可选，默认高精度，设置定位模式，高精度，低功耗，仅设备

        option.setCoorType("bd09ll")
        //可选，默认gcj02，设置返回的定位结果坐标系

        val span = 1000
        option.setScanSpan(span)
        //可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的

        option.setIsNeedAddress(true)
        //可选，设置是否需要地址信息，默认不需要

        option.isOpenGps = true
        //可选，默认false,设置是否使用gps

        option.isLocationNotify = true
        //可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果

        option.setIsNeedLocationDescribe(true)
        //可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”

        option.setIsNeedLocationPoiList(true)
        //可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到

        option.setIgnoreKillProcess(false)
        //可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死

        option.isIgnoreCacheException = false
        //可选，默认false，设置是否收集CRASH信息，默认收集

        option.setEnableSimulateGps(false)
        //可选，默认false，设置是否需要过滤GPS仿真结果，默认需要

//        option.setWifiValidTime(5 * 60 * 1000)
        //可选，7.2版本新增能力，如果您设置了这个接口，首次启动定位时，会先判断当前WiFi是否超出有效期，超出有效期的话，会先重新扫描WiFi，然后再定位

        mLocationClient!!.setLocOption(option)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun sendResult(event: SendMsgResultEvent) {
        sendBtn.isClickable = true
        if (event.isOk) {
            sendBtn.setBackgroundResource(R.drawable.circle_btn_bg_succed)
            var animator = AnimationSet(true)


        } else {
            sendBtn.setBackgroundResource(R.drawable.circle_btn_bg_fail)
            Observable.timer(2, TimeUnit.SECONDS)
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        sendBtn.setBackgroundResource(R.drawable.circle_btn_bg_normal)
                    }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            var rotation = RotateAnimation(0f, 90f
//                    , sendBtn.width / 2f, sendBtn.height / 2f)
//            rotation.duration = 2000
//            sendBtn.startAnimation(rotation)
//            sendBtn.animate()
//                    .rotation(90f)
//                    .setInterpolator(AnticipateOvershootInterpolator())
//            sendBtn.animate().translationZ(100f)
            var scaleAnimation = ScaleAnimation(1f
                    , 0.8f
                    , 1f
                    , 1.5f
                    , Animation.RELATIVE_TO_SELF
                    , 0.5f
                    , Animation.RELATIVE_TO_SELF
                    , 0.5f)
            scaleAnimation.duration = 2000
            scaleAnimation.interpolator = AnticipateInterpolator()
            scaleAnimation.fillAfter = true
            sendBtn.startAnimation(scaleAnimation)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}

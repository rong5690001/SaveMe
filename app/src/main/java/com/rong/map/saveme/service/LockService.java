package com.rong.map.saveme.service;

import android.app.KeyguardManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.WindowManager;

import com.blankj.utilcode.util.LogUtils;
import com.rong.map.saveme.LockActivity;

/**
 * 作者：陈华榕
 * 邮箱:mpa.chen@sportq.com
 * 时间：2017/8/9  18:24
 */

public class LockService extends Service {

    private static final String TAG = "LockService";
    WindowManager windowManager;

    private Intent lockIntent = null;
    private KeyguardManager mKeyguardManager = null;
    private KeyguardManager.KeyguardLock mKeyguardLock = null;

    @Override
    public void onCreate() {
        super.onCreate();

        lockIntent = new Intent(this, LockActivity.class);
        lockIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        /* 注册广播 */
        IntentFilter mScreenOnFilter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        LockService.this.registerReceiver(mScreenOnReceiver, mScreenOnFilter);

        /* 注册广播 */
        IntentFilter mScreenOffFilter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        LockService.this.registerReceiver(mScreenOffReceiver, mScreenOffFilter);

//        createView();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mScreenOnReceiver);
        unregisterReceiver(mScreenOffReceiver);
        startService(new Intent(this, LockService.class));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    // 屏幕变亮的广播,我们要隐藏默认的锁屏界面
    private BroadcastReceiver mScreenOnReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
                LogUtils.i(TAG, "----------------- Intent.ACTION_SCREEN_ON------");
            }
        }
    };

    // 屏幕变暗/变亮的广播 ， 我们要调用KeyguardManager类相应方法去解除屏幕锁定
    private BroadcastReceiver mScreenOffReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_SCREEN_OFF) || action.equals(Intent.ACTION_SCREEN_ON)) {
                mKeyguardManager = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
                mKeyguardLock = mKeyguardManager.newKeyguardLock("");
                mKeyguardLock.disableKeyguard();
                startActivity(lockIntent);
            }
        }
    };

//    public void createView() {
//
//        WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();
//        windowManager = (WindowManager) getApplication()
//                .getSystemService(getApplication().WINDOW_SERVICE);
//
//        windowParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
//        windowParams.gravity = Gravity.LEFT | Gravity.TOP;
//
//        windowParams.x = 0;
//        windowParams.y = 0;
//
//        windowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
//        windowParams.height = WindowManager.LayoutParams.MATCH_PARENT;
//
//        LayoutInflater inflater = LayoutInflater.from(this);
//
//        View contentView = inflater.inflate(R.layout.dialog_lock, null);
//
//        contentLayout = (RelativeLayout) contentView.findViewById(R.id.contentLayout);
//        windowManager.addView(contentLayout, windowParams);
//        contentLayout.setOnTouchListener(new View.OnTouchListener() {
//            @Override
//            public boolean onTouch(View view, MotionEvent motionEvent) {
//                windowManager.removeView(contentLayout);
//                return false;
//            }
//        });
//
//        contentLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.AT_MOST)
//                , View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.AT_MOST));
//
//    }
}

package com.rong.map.saveme.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import com.rong.map.saveme.R;

/**
 * 作者：陈华榕
 * 邮箱:mpa.chen@sportq.com
 * 时间：2017/8/9  18:24
 */

public class LockService extends Service {

    WindowManager windowManager;
    LinearLayout contentLayout;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public void createView() {

        WindowManager.LayoutParams windowParams = new WindowManager.LayoutParams();
        windowManager = (WindowManager) getApplication()
                .getSystemService(getApplication().WINDOW_SERVICE);

        windowParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_DIALOG;
        windowParams.gravity = Gravity.LEFT | Gravity.TOP;
        windowParams.x = 0;
        windowParams.y = 0;
        windowParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        windowParams.height = WindowManager.LayoutParams.MATCH_PARENT;

        LayoutInflater inflater = LayoutInflater.from(this);

        View contentView = inflater.inflate(R.layout.dialog_lock, null);

        contentLayout = (LinearLayout) contentView.findViewById(R.id.contentLayout);
        windowManager.addView(contentLayout, windowParams);
        contentView.findViewById(R.id.button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                windowManager.removeView(contentLayout);
            }
        });
        contentLayout.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.AT_MOST)
                , View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.AT_MOST));


    }
}

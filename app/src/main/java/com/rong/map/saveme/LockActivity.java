package com.rong.map.saveme;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.rong.map.saveme.service.LockService;
import com.rong.map.saveme.utils.CstUtils;
import com.rong.map.saveme.widget.LockView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LockActivity extends BaseActivity {
    Intent intent;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.lockView)
    LockView mLockView;
    private int confirm = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        ButterKnife.bind(this);
        intent = new Intent(this, LockService.class);
        mLockView.setSetPsd(true);
        mLockView.setOnLockListener(new LockView.OnLockListener() {
            @Override
            public void onSucceed(String results) {
                confirm--;
                if (confirm == 0) {//再次确认成功
                    SPUtils.getInstance(CstUtils.TABLENAME).put(CstUtils.KEY_PASSWORD, results);
                }
                mTitle.setTextColor(Color.parseColor("#3b3b3b"));
                if (getStringRes(R.string.setyourpassword)
                        .equals(mTitle.getText().toString())) {
                    mTitle.setText(R.string.confirmpassword);
                } else if (getStringRes(R.string.confirmpassword)
                        .equals(mTitle.getText().toString())) {
                    mTitle.setText(R.string.setpswscd);
                }
            }

            @Override
            public void onError() {
                confirm = 2;
                mTitle.setTextColor(Color.RED);
                mTitle.setText(R.string.lckViewErrorMs);
            }
        });
//        startService(intent);
    }

    @Override
    protected void onDestroy() {
//        stopService(intent);
        super.onDestroy();
    }
}

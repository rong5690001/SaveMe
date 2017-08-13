package com.rong.map.saveme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.blankj.utilcode.util.SPUtils;
import com.blankj.utilcode.util.StringUtils;
import com.rong.map.saveme.service.LockService;
import com.rong.map.saveme.utils.CstUtils;
import com.rong.map.saveme.widget.LockView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LockActivity extends AppCompatActivity {
    Intent intent;
    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.lockView)
    LockView mLockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        ButterKnife.bind(this);
        intent = new Intent(this, LockService.class);
        if(StringUtils.isEmpty(SPUtils.getInstance(CstUtils.TABLENAME).getString(CstUtils.KEY_PASSWORD))){
            mTitle.setVisibility(View.VISIBLE);
        } else {
            mTitle.setVisibility(View.GONE);
        }
//        startService(intent);
    }

    @Override
    protected void onDestroy() {
//        stopService(intent);
        super.onDestroy();
    }
}

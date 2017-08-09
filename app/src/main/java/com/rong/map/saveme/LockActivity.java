package com.rong.map.saveme;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.rong.map.saveme.service.LockService;

public class LockActivity extends AppCompatActivity {
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        intent = new Intent(this, LockService.class);
//        startService(intent);
    }

    @Override
    protected void onDestroy() {
//        stopService(intent);
        super.onDestroy();
    }
}

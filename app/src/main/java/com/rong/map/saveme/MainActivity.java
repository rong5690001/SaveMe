package com.rong.map.saveme;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.rong.map.saveme.manager.SharePreferencesManager;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    /**
     * 紧急联系人电话
     */
    private EditText mPhone;
    /**
     * 短信内容
     */
    private EditText mContent;
    /**
     * 保存
     */
    private Button mSaveBtn;
    private PendingIntent sentPI;
    private PendingIntent deliverPI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initSMS();
    }

    private void initView() {
        mPhone = (EditText) findViewById(R.id.phone);
        mPhone.setText(SharePreferencesManager.getPhoneNum());
        mContent = (EditText) findViewById(R.id.content);
        mContent.setText(SharePreferencesManager.getContent());
        mSaveBtn = (Button) findViewById(R.id.saveBtn);
        mSaveBtn.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.saveBtn:
                if(canSave()){
                    sendSMS(mPhone.getText().toString(), mContent.getText().toString());
                } else {
                    Toast.makeText(MainActivity.this,
                            "请输入完成内容", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
        }
    }

    private void initSMS(){
        //处理返回的发送状态
        String SENT_SMS_ACTION = "SENT_SMS_ACTION";
        Intent sentIntent = new Intent(SENT_SMS_ACTION);
        sentPI= PendingIntent.getBroadcast(this, 0, sentIntent,
                0);
        // register the Broadcast Receivers
        this.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context _context, Intent _intent) {
                switch (getResultCode()) {
                    case Activity.RESULT_OK:
                        SharePreferencesManager.putPhoneNum(mPhone.getText().toString());
                        Toast.makeText(MainActivity.this,
                                "短信发送成功", Toast.LENGTH_SHORT)
                                .show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        break;
                    case SmsManager.RESULT_ERROR_RADIO_OFF:
                        break;
                    case SmsManager.RESULT_ERROR_NULL_PDU:
                        break;
                }
            }
        }, new IntentFilter(SENT_SMS_ACTION));

        //处理返回的接收状态
        String DELIVERED_SMS_ACTION = "DELIVERED_SMS_ACTION";
        // create the deilverIntent parameter
        Intent deliverIntent = new Intent(DELIVERED_SMS_ACTION);
        deliverPI = PendingIntent.getBroadcast(this, 0,
                deliverIntent, 0);
        this.registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context _context, Intent _intent) {
                Toast.makeText(MainActivity.this,
                        "收信人已经成功接收", Toast.LENGTH_SHORT)
                        .show();
            }
        }, new IntentFilter(DELIVERED_SMS_ACTION));
    }

    /**
     * 直接调用短信接口发短信
     * @param phoneNumber
     * @param message
     */
    public void sendSMS(String phoneNumber,String message){
        SharePreferencesManager.putContent(mContent.getText().toString());
        //获取短信管理器
        android.telephony.SmsManager smsManager = android.telephony.SmsManager.getDefault();
        //拆分短信内容（手机短信长度限制）
        List<String> divideContents = smsManager.divideMessage(message);
        for (String text : divideContents) {
            smsManager.sendTextMessage(phoneNumber, null, text, sentPI, deliverPI);
        }
    }

    private boolean canSave(){
        if(TextUtils.isEmpty(mPhone.getText().toString())) return false;
        if(TextUtils.isEmpty(mContent.getText().toString())) return false;
        return true;
    }
}

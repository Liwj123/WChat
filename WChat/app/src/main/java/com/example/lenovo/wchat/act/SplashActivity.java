package com.example.lenovo.wchat.act;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;

import com.example.lenovo.wchat.R;
import com.hyphenate.chat.EMClient;

/**
 * Created by Lenovo on 2017/4/20.
 */

public class SplashActivity extends BaseActivity {

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //判断       单例模式的方法名   是否之前登陆过
            if (EMClient.getInstance().isLoggedInBefore()) {
                intentToMain();
            } else {
                intentToLogin();
            }
            finish();
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        handler.sendEmptyMessageDelayed(1, 3000);
        findViewById(R.id.splash_img).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.removeMessages(1);
            }
        });
    }
}

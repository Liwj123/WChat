package com.example.lenovo.wchat.act;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import com.example.lenovo.wchat.R;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import org.w3c.dom.Text;

/**
 * Created by Lenovo on 2017/4/27.
 */

public class RegisterActivity extends BaseActivity {
    private EditText user_et;
    private EditText pass_et;
    private EditText repeat_et;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    toast("注册成功");
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        user_et = (EditText) findViewById(R.id.et_user);
        pass_et = (EditText) findViewById(R.id.et_pass);
        repeat_et = (EditText) findViewById(R.id.et_repeat);
    }

    public void register(View view) {
        final String user = user_et.getText().toString();
        final String pass = pass_et.getText().toString();
        final String repeat = repeat_et.getText().toString();

        if (TextUtils.isEmpty(user) || TextUtils.isEmpty(pass) || user.length() < 4 || pass.length() < 4) {
            toast("账号或密码格式不正确");
            return;
        }

        if (!pass.equals(repeat)) {
            toast("两次输入的密码不同");
            return;
        }

        new Thread() {
            @Override
            public void run() {
                try {
                    EMClient.getInstance().createAccount(user, pass);
                    handler.sendEmptyMessage(1);
                    finish();
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}

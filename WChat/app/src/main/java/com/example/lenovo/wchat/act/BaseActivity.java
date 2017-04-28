package com.example.lenovo.wchat.act;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Lenovo on 2017/4/20.
 */

public class BaseActivity extends AppCompatActivity {
    public void intentToLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    public void intentToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}

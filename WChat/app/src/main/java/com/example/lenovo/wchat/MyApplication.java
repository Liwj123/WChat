package com.example.lenovo.wchat;

import android.app.Application;

import com.example.lenovo.wchat.Utils.StringUtil;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;

/**
 * Created by Lenovo on 2017/4/20.
 * 初始化sdk，工具类时在本页面，属于本程序的入口
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initEMsdk();
        StringUtil.list2Map();
    }

    private void initEMsdk() {
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        //初始化
        EMClient.getInstance().init(this, options);
        //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
        EMClient.getInstance().setDebugMode(true);
    }

}

package com.example.lenovo.wchat.Utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Lenovo on 2017/4/27.
 */

public class SPUtil {
    private static final String SP_NAME = "user";
    private static final String USER_KEY = "userName";
    private static SharedPreferences sp;
    private static final String CHAT_DEFF = "chatdeff";

    public static String getChatDeff(Context context) {
        getSP(context);
        return sp.getString(CHAT_DEFF, "");
    }

    public static void setChatDeff(Context context, String json) {
        getSP(context);
        sp.edit().putString(CHAT_DEFF, json).apply();
    }

    public static void setLoginUser(Context context, String user) {
        getSP(context);
        sp.edit().putString(USER_KEY, user).apply();
    }

    private static void getSP(Context context) {
        if (sp == null) {
            sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        }
    }

    public static String getLoginUser(Context context) {
        getSP(context);
        return sp.getString(USER_KEY, "");
    }
}

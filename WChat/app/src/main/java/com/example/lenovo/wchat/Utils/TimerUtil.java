package com.example.lenovo.wchat.Utils;

/**
 * Created by Lenovo on 2017/6/13.
 */

public class TimerUtil {
    public static String voiceTime(int time){
        String times;
        if(time>=60){
            int minute = time / 60;
            int second = time % 60;
            times=minute+"'"+second+"\"";
        }else{
            times=time+"\"";
        }
        return times;
    }

    public static String sendTime(int time) {
        String times;
        if (time >= 60) {
            int minute = time / 60;
            int second = time % 60;
            if (second < 10) {
                times = minute + ":0" + second;
            } else {
                times = minute + ":" + second;
            }
        } else {
            if (time < 10) {
                times = "0:0" + time;
            } else {
                times = "0:" + time;
            }
        }
        return times;
    }
}

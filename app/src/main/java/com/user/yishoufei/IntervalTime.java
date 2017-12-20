package com.user.yishoufei;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by User on 2017/6/15.
 */

public class IntervalTime {
    String IntervalTime;
    long still_minutes;
    public IntervalTime() {
    }

    public String get_IntervalTime(String Start, String End) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date d1 = df.parse(Start);
            Date d2 = df.parse(End);
//Date d2 = new Date(System.currentTimeMillis());//你也可以获取当前时间
            long diff = d2.getTime()-d1.getTime(); //这样得到的差值是微秒级别
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);
            IntervalTime = "" + days + "天" + hours + "小时" + minutes + "分";

        } catch (Exception e) {
        }
        return IntervalTime;
    }

    public long get_still_minutes(String Start, String End){

        DateFormat time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
        Date d1 = time.parse(Start);
        Date d2 = time.parse(End);
//Date d2 = new Date(System.currentTimeMillis());//你也可以获取当前时间
        long diff = d2.getTime()-d1.getTime(); //这样得到的差值是微秒级别
        still_minutes = diff/(1000 * 60);
        } catch (Exception e) {
        }
        return still_minutes;

    }
    public String getCurrentdate(){
        DateFormat df = new SimpleDateFormat("yyyyMMdd");
        String current =df.format(new Date());
        Log.d("current",current+"======");
        return  current+"";

    }
}

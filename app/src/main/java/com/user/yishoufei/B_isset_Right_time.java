package com.user.yishoufei;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class B_isset_Right_time {

    SimpleDateFormat sdf=new SimpleDateFormat("HH:mm");
    //比较两个时间的大小
    public Boolean is_right_set(String day_start_time ,String day_end_time) throws ParseException{

        Date start_time =sdf.parse(day_start_time);
        Date end_time = sdf.parse(day_end_time);
        //Date类的一个方法，如果a早于b返回true，否则返回false
        if(start_time.before(end_time))
            return true;
        else
            return false;
    }
    //判断是否在白天范围内
    public Boolean is_day_time(String Park_Start_Time,String day_start_time ,String day_end_time) throws ParseException{
       Date park_Start_Time =sdf.parse(Park_Start_Time);
        Date start_time =sdf.parse(day_start_time);
        Date end_time = sdf.parse(day_end_time);
        if (park_Start_Time.after(start_time) && park_Start_Time.before(end_time)) {
            return true;
        } else {
            return false;
        }

    }

//    public static void main(String[] args) throws Exception
//    {
//        boolean result=new B_isset_Right_time().is_right_set("16:19:16", "16:18:18");
//        System.out.println(result);
//    }

}

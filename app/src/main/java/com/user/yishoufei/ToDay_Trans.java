package com.user.yishoufei;

import org.litepal.crud.DataSupport;

import java.sql.Time;
import java.util.Date;

/**
 * Created by User on 2017/6/13.
 */

public class ToDay_Trans extends DataSupport {
    private String Car_Num;
    private Date Start_Time;
    private Date End_Time;
    private float Money;

    public ToDay_Trans(String car_Num, Date start_Time, Date end_Time, float money) {
        Car_Num = car_Num;
        Start_Time = start_Time;
        End_Time = end_Time;
        Money = money;
    }

    public String getCar_Num() {
        return Car_Num;
    }

    public void setCar_Num(String car_Num) {
        Car_Num = car_Num;
    }

    public Date getStart_Time() {
        return Start_Time;
    }

    public void setStart_Time(Time start_Time) {
        Start_Time = start_Time;
    }

    public Date getEnd_Time() {
        return End_Time;
    }

    public void setEnd_Time(Time end_Time) {
        End_Time = end_Time;
    }

    public float getMoney() {
        return Money;
    }

    public void setMoney(float money) {
        Money = money;
    }
}

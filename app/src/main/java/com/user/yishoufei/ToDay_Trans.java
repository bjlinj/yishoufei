package com.user.yishoufei;

import java.sql.Time;

/**
 * Created by User on 2017/6/13.
 */

public class ToDay_Trans {
    private String Car_Num;
    private Time Start_Time;
    private Time End_Time;
    private float Money;

    public ToDay_Trans(String car_Num, Time start_Time, Time end_Time, float money) {
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

    public Time getStart_Time() {
        return Start_Time;
    }

    public void setStart_Time(Time start_Time) {
        Start_Time = start_Time;
    }

    public Time getEnd_Time() {
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

package com.user.yishoufei;

import org.litepal.crud.DataSupport;

import java.sql.Time;
import java.util.Date;
import java.util.UUID;

/**
 * Created by User on 2017/6/13.
 */

public class ToDay_Trans extends DataSupport {

    private String Id;
    private String Car_Num;
    private String Start_Time;
    private String End_Time;
    private String Money;
    public ToDay_Trans(String car_Num, String start_Time, String end_Time, String money) {
        Id=UUID.randomUUID().toString();
        Car_Num = car_Num;
        Start_Time = start_Time;
        End_Time = end_Time;
        Money = money;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = UUID.randomUUID().toString();
    }
    public String getCar_Num() {
        return Car_Num;
    }

    public void setCar_Num(String car_Num) {
        Car_Num = car_Num;
    }

    public String getStart_Time() {
        return Start_Time;
    }

    public void setStart_Time(String start_Time) {
        Start_Time = start_Time;
    }

    public String getEnd_Time() {
        return End_Time;
    }

    public void setEnd_Time(String end_Time) {
        End_Time = end_Time;
    }

    public String getMoney() {
        return Money;
    }

    public void setMoney(String money) {
        Money = money;
    }
}

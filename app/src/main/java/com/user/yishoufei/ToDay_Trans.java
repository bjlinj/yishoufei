package com.user.yishoufei;

import org.litepal.crud.DataSupport;

import java.sql.Time;
import java.util.Date;
import java.util.UUID;

/**
 * Created by User on 2017/6/13.
 */

public class ToDay_Trans extends DataSupport {

    private long Id;
    private String  Type_Cord;
    private String Car_Num;
    private String Start_Date;
    private String Start_Time;
    private String End_Time;
    private String End_Date;
    private String Money;
    public ToDay_Trans(String car_Num,String start_date,String start_Time, String end_date,String end_Time, String money,String type_Cord) {
        Car_Num=car_Num;
        Start_Time = start_Time;
        End_Time = end_Time;
        Money = money;
        Type_Cord=type_Cord;
        Start_Date=start_date;
        End_Date=end_date;
    }

    public String getType_Cord() {
        return Type_Cord;
    }

    public void setType_Cord(String type_Cord) {
        Type_Cord = type_Cord;
    }

    public long getId() {
        return Id;
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

    public String getStart_Date() {
        return Start_Date;
    }

    public void setStart_Date(String start_Date) {
        Start_Date = start_Date;
    }

    public String getEnd_Date() {
        return End_Date;
    }

    public void setEnd_Date(String end_Date) {
        End_Date = end_Date;
    }
}

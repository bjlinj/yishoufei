package com.user.yishoufei;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * Created by User on 2017/6/17.
 */

public class RuleConfig  extends DataSupport implements Serializable {
    private static final long serialVersionUID= 1L;

    private double  First_hour;//首X小时收费默认为1
    private double  First_yuan_hour;//首1小时收费1.5元/X分钟
    private double  First_min;//首X小时收费1.5元/15分钟
    private double  After_yuan_hour;//X小时后X元/分钟
   // private double  UnitPrice;//暂时设置一小时2元
    private double FreePrice;//设置免费分钟数



    public double getFirst_hour() {
        return First_hour;
    }

    public void setFirst_hour(double first_hour) {
        First_hour = first_hour;
    }

    public double getFirst_yuan_hour() {
        return First_yuan_hour;
    }

    public void setFirst_yuan_hour(double first_yuan_hour) {
        First_yuan_hour = first_yuan_hour;
    }

    public double getFirst_min() {
        return First_min;
    }

    public void setFirst_min(double first_min) {
        First_min = first_min;
    }

    public double getAfter_yuan_hour() {
        return After_yuan_hour;
    }

    public void setAfter_yuan_hour(double after_yuan_hour) {
        After_yuan_hour = after_yuan_hour;
    }



    public double getFreePrice() {
        return FreePrice;
    }

    public void setFreePrice(double freePrice) {
        FreePrice = freePrice;
    }


    public RuleConfig() {
        super();
    }





}

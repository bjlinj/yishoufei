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
    private double  After_hour_min;//白天x小时后x元after_hour_min分钟
   // private double  UnitPrice;//暂时设置一小时2元
    private double FreePrice;//设置免费分钟数
    private String Day_start_time; //设置白天开始时间
    private String Day_end_time;//设置白天结束时间

    private double  N_First_hour;//晚上首X小时收费默认为1
    private double  N_First_yuan_hour;//晚上首1小时收费1.5元/X分钟
    private double  N_First_min;//晚上首X小时收费1.5元/15分钟
    private double  N_After_yuan_hour;//晚上X小时后X元/分钟
    private double N_FreePrice;//晚上设置免费分钟数
    private double N_after_hour_min;//晚上x小时后x元after_hour_min分钟
    private String is_open_post;//是否档位收费
    private double max_cost;//单次最高收费金额

    public String getIs_open_post() {
        return is_open_post;
    }

    public void setIs_open_post(String is_open_post) {
        this.is_open_post = is_open_post;
    }

    public double getMax_cost() {
        return max_cost;
    }

    public void setMax_cost(double max_cost) {
        this.max_cost = max_cost;
    }

    public double getAfter_hour_min() {
        return After_hour_min;
    }

    public void setAfter_hour_min(double after_hour_min) {
        After_hour_min = after_hour_min;
    }

    public double getN_after_hour_min() {
        return N_after_hour_min;
    }

    public void setN_after_hour_min(double n_after_hour_min) {
        N_after_hour_min = n_after_hour_min;
    }

    public String getDay_start_time() {
        return Day_start_time;
    }

    public void setDay_start_time(String day_start_time) {
        Day_start_time = day_start_time;
    }

    public String getDay_end_time() {
        return Day_end_time;
    }

    public void setDay_end_time(String day_end_time) {
        Day_end_time = day_end_time;
    }

    public double getN_First_hour() {
        return N_First_hour;
    }

    public void setN_First_hour(double n_First_hour) {
        N_First_hour = n_First_hour;
    }

    public double getN_First_yuan_hour() {
        return N_First_yuan_hour;
    }

    public void setN_First_yuan_hour(double n_First_yuan_hour) {
        N_First_yuan_hour = n_First_yuan_hour;
    }

    public double getN_First_min() {
        return N_First_min;
    }

    public void setN_First_min(double n_First_min) {
        N_First_min = n_First_min;
    }

    public double getN_After_yuan_hour() {
        return N_After_yuan_hour;
    }

    public void setN_After_yuan_hour(double n_After_yuan_hour) {
        N_After_yuan_hour = n_After_yuan_hour;
    }

    public double getN_FreePrice() {
        return N_FreePrice;
    }

    public void setN_FreePrice(double n_FreePrice) {
        N_FreePrice = n_FreePrice;
    }

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

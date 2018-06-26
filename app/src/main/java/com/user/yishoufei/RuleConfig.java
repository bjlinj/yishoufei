package com.user.yishoufei;

import org.litepal.crud.DataSupport;

/**
 * Created by User on 2017/6/17.
 */

public class RuleConfig extends DataSupport {

    private double  UnitPrice;//暂时设置一小时2元

    private double FreePrice =0;//设置免费分钟数

    public double getFreePrice() {
        return FreePrice;
    }

    public void setFreePrice(double freePrice) {
        FreePrice = freePrice;
    }


    public RuleConfig() {
        super();
    }

    public double  getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        UnitPrice = unitPrice;
    }



}

package com.user.yishoufei;

import org.litepal.crud.DataSupport;

/**
 * Created by User on 2017/6/17.
 */

public class RuleConfig extends DataSupport {

    private int UnitPrice=2;//暂时设置一小时2元

    public RuleConfig() {
    }

    public int getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(int unitPrice) {
        UnitPrice = unitPrice;
    }



}

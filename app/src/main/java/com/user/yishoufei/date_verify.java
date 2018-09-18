package com.user.yishoufei;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class date_verify  extends DataSupport implements Serializable {

    private long id;
    private String verify_date;
    private int end_months_day=30;
    private int end_year_day=365;
    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
    Calendar c = Calendar.getInstance();

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getVerify_date() {
        return verify_date;
    }

    public void setVerify_date(String verify_date) {
        this.verify_date = verify_date;
    }

    //计算到期时间
    public   String getexpire_month(){

        c.add(Calendar.DAY_OF_MONTH, end_months_day);//限制30天的试用时间
        //System.out.println("当前时间+1"+sf.format(c.getTime()));
        return sf.format(c.getTime());
    }

    public   String getexpire_year(){

        c.add(Calendar.DAY_OF_MONTH, end_year_day);//限制30天的试用时间
        //System.out.println("当前时间+1"+sf.format(c.getTime()));
        return sf.format(c.getTime());
    }
}

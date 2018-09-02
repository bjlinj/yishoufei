package com.user.yishoufei;

import org.litepal.crud.DataSupport;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class date_verify  extends DataSupport implements Serializable {

    private String verify_date;
    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
    Calendar c = Calendar.getInstance();

    public String getVerify_date() {
        return verify_date;
    }

    public void setVerify_date(String verify_date) {
        this.verify_date = verify_date;
    }

    //计算到期时间
    public   String getexpire(){

        c.add(Calendar.DAY_OF_MONTH, 30);//限制30天的试用时间
        //System.out.println("当前时间+1"+sf.format(c.getTime()));
        return sf.format(c.getTime());
    }
}

package com.user.yishoufei;

import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by User on 2017/6/19.
 */

public class Random_Num extends DataSupport {

    private String strRand="";



    public String getRandom_Num(){
        //产生3位的随机数
        for(int i=0;i<3;i++){

            strRand += String.valueOf((int)(Math.random() * 10)) ;
        }
        return strRand;
    }

    public String getStrRand() {
        return strRand;
    }

    public void setStrRand(String strRand) {
        this.strRand = strRand;
    }


}



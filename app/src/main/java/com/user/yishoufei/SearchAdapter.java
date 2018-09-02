package com.user.yishoufei;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class SearchAdapter extends BaseAdapter {

    private List<ToDay_Trans> list;
    private LayoutInflater inflater;
    SimpleDateFormat formatter_date = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat formatter_Time = new SimpleDateFormat("HH:mm:ss");
    public SearchAdapter(MainActivity context, List<ToDay_Trans> list){
        this.list = new ArrayList(list);
//        Collections.sort(list,new SortComparator());
        for(ToDay_Trans attribute : list) {
            System.out.println(attribute.getCar_Num()+"=="+new SortComparator());
        }

        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        int ret = 0;
        if(list!=null){
            ret = list.size();
        }
        return ret;
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ToDay_Trans today = (ToDay_Trans) this.getItem(position);
        //Collections.sort(list,new SortComparator());//排序处理
        ViewHolder viewHolder;
        String st;

        if(convertView == null){

            viewHolder = new ViewHolder();

            convertView = inflater.inflate(R.layout.search_list, null);
            viewHolder.Id = (TextView) convertView.findViewById(R.id.id);
            viewHolder.TodayCar_Num = (TextView) convertView.findViewById(R.id.car_num);
            viewHolder.TodayStart_Time = (TextView) convertView.findViewById(R.id.start_time);
            viewHolder.Still_Time = (TextView) convertView.findViewById(R.id.still_time);
            viewHolder.IS_Steal=(TextView) convertView.findViewById(R.id.is_Steal);
            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.TodayCar_Num.setText(today.getCar_Num());
        viewHolder.TodayCar_Num.setTextSize(16);
        viewHolder.TodayStart_Time.setText(today.getStart_Date()+"\n"+today.getStart_Time().toString());
        viewHolder.TodayStart_Time.setTextSize(16);
        Date curDate = new Date(System.currentTimeMillis());
        String start =today.getStart_Date()+" "+today.getStart_Time();
        String end = today.getEnd_Date()+" "+today.getEnd_Time();
        IntervalTime intervaltime = new IntervalTime();
        String interval = intervaltime.get_IntervalTime(start, end);
        viewHolder.Still_Time.setText(interval);
        viewHolder.Still_Time.setTextSize(16);
        if(today.getIs_Steal().equals("1")){
             st="是";
        }else{
             st="否";
        }
        viewHolder.IS_Steal.setText(st);
        viewHolder.IS_Steal.setTextSize(16);
        //viewHolder.goodMoney.setText(goods.getMoney()+"");
        //viewHolder.goodMoney.setTextSize(13);

        return convertView;
    }

    public static class ViewHolder{
        public TextView Id;
        public TextView TodayCar_Num;
        public TextView TodayStart_Time;
        public TextView Still_Time;
        public TextView TodayMoney;
        public TextView IS_Steal;
    }



}

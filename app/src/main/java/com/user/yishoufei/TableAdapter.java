package com.user.yishoufei;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by User on 2017/6/11.
 */

public class TableAdapter extends BaseAdapter {

    private List<ToDay_Trans> list;
    private LayoutInflater inflater;

    public TableAdapter(MainActivity context, List<ToDay_Trans> list){
        this.list = list;
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

        ViewHolder viewHolder;

        if(convertView == null){

            viewHolder = new ViewHolder();

            convertView = inflater.inflate(R.layout.list_item, null);
            viewHolder.TodayCar_Num = (TextView) convertView.findViewById(R.id.text_today_name);
            viewHolder.TodayStart_Time = (TextView) convertView.findViewById(R.id.text_codeBar);
            viewHolder.TodayEnd_Time = (TextView) convertView.findViewById(R.id.text_num);
            viewHolder.TodayMoney = (TextView) convertView.findViewById(R.id.text_curPrice);
            //viewHolder.goodMoney = (TextView) convertView.findViewById(R.id.text_money);

            convertView.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.TodayCar_Num.setText(today.getCar_Num());
        viewHolder.TodayCar_Num.setTextSize(13);
        viewHolder.TodayStart_Time.setText(today.getStart_Time().toString());
        viewHolder.TodayStart_Time.setTextSize(13);
        viewHolder.TodayEnd_Time.setText(today.getEnd_Time()+"");
        viewHolder.TodayEnd_Time.setTextSize(13);
        viewHolder.TodayMoney.setText(today.getMoney()+"");
        viewHolder.TodayMoney.setTextSize(13);
        //viewHolder.goodMoney.setText(goods.getMoney()+"");
        //viewHolder.goodMoney.setTextSize(13);

        return convertView;
    }

    public static class ViewHolder{
        public TextView TodayCar_Num;
        public TextView TodayStart_Time;
        public TextView TodayEnd_Time;
        public TextView TodayMoney;
    }

}

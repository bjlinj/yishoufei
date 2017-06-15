package com.user.yishoufei;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.user.yishoufei.R;
import com.user.yishoufei.ToDay_Trans;

import java.util.List;

/**
 * Created by User on 2017/6/15.
 */

public class TodayAdapter extends ArrayAdapter<ToDay_Trans> {

    private int resourceId;

    public TodayAdapter(Context context, int textViewResourceId,
                        List<ToDay_Trans> objects) {
        super(context, textViewResourceId, objects);
        resourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ToDay_Trans today = getItem(position); // 获取当前项的Fruit实例
        View view;
        ViewHolder viewHolder;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.Id = (TextView) convertView.findViewById(R.id.id);
            viewHolder.TodayCar_Num = (TextView) convertView.findViewById(R.id.text_today_name);
            viewHolder.TodayStart_Time = (TextView) convertView.findViewById(R.id.text_codeBar);
            viewHolder.TodayEnd_Time = (TextView) convertView.findViewById(R.id.text_num);
            viewHolder.TodayMoney = (TextView) convertView.findViewById(R.id.text_curPrice);
            view.setTag(viewHolder); // 将ViewHolder存储在View中
        } else {
            view = convertView;
            viewHolder = (ViewHolder) view.getTag(); // 重新获取ViewHolder
        }
        viewHolder.TodayCar_Num.setText(today.getCar_Num());
        viewHolder.TodayCar_Num.setTextSize(13);
        viewHolder.TodayStart_Time.setText(today.getStart_Time().toString());
        viewHolder.TodayStart_Time.setTextSize(13);
        viewHolder.TodayEnd_Time.setText(today.getEnd_Time()+"");
        viewHolder.TodayEnd_Time.setTextSize(13);
        viewHolder.TodayMoney.setText(today.getMoney()+"");
        viewHolder.TodayMoney.setTextSize(13);
        return view;
    }

    class ViewHolder {
        public TextView Id;
        public TextView TodayCar_Num;
        public TextView TodayStart_Time;
        public TextView TodayEnd_Time;
        public TextView TodayMoney;

    }
}

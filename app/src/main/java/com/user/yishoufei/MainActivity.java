package com.user.yishoufei;

import android.content.DialogInterface;
import android.graphics.Color;
import android.icu.text.LocaleDisplayNames;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;
import org.litepal.tablemanager.Connector;
import org.litepal.util.Const;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;



public class MainActivity extends AppCompatActivity {

    private View homeview;
    private View web_ui;
    private WebView webView;
    private Button Start_Money;
    private Button End_Money;
    private EditText Input_Mess_Start;
    private EditText Input_Mess_End;
    private List<ToDay_Trans> list;
    private ListView tableListView;
    private long get_id;
    private Button Refresh;
    private Spinner Spinner_City;
    private ArrayAdapter adapter;
    SimpleDateFormat formatter_date = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat formatter_Time = new SimpleDateFormat("HH:mm:ss");

    //刷新列表
    public void fresh() {
        list = DataSupport.where("Type_Cord = ?", "0").order("Start_Time desc").find(ToDay_Trans.class);
        tableListView = (ListView) findViewById(R.id.list);
        //TodayAdapter adapter = new TodayAdapter(MainActivity.this,R.layout.list_item, list);
        TableAdapter adapter = new TableAdapter(MainActivity.this, list);
        tableListView.setAdapter(adapter);
    }

    //按条件模糊查询
    public void selfresh(String s) {
        list = DataSupport.order("Start_Date desc").order("Start_Time desc").where("Car_Num like ? ", "%" + s + "%").find(ToDay_Trans.class);
        tableListView = (ListView) findViewById(R.id.list);
        //TodayAdapter adapter = new TodayAdapter(MainActivity.this,R.layout.list_item, list);
        TableAdapter adapter = new TableAdapter(MainActivity.this, list);
        tableListView.setAdapter(adapter);
    }

    //用ID精确查找
    public List<ToDay_Trans> sreachbyid(long s) {
        list = DataSupport.findAll(ToDay_Trans.class, s);
        return list;
    }

    //按ID删除数据
    public void delfresh(long s) {
        DataSupport.delete(ToDay_Trans.class, s);
        fresh();//删除之后刷新一下数据
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    //home_button.setText(R.string.title_home);
                    homeview.setVisibility(View.VISIBLE);
                    web_ui.setVisibility(View.GONE);
                    return true;
                case R.id.navigation_dashboard:
                    homeview.setVisibility(View.GONE);
                    web_ui.setVisibility(View.VISIBLE);
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.setWebViewClient(new WebViewClient());
                    webView.loadUrl("http://news.sina.com.cn/");
                    return true;
                case R.id.navigation_notifications:
                    // mTextMessage.setText(R.string.title_notifications);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LitePal.getDatabase();
        ActionBar actionBar = getSupportActionBar();//隐藏默认的控件
        if (actionBar != null) {
            actionBar.hide();
        }
        homeview = findViewById(R.id.include_home);
        web_ui = findViewById(R.id.include_view);
        webView = (WebView) findViewById(R.id.web_view);
        Start_Money = (Button) findViewById(R.id.Button_Start);
        End_Money = (Button) findViewById(R.id.Button_End);
        Input_Mess_Start = (EditText) findViewById(R.id.Input_Mess_Start);
        Input_Mess_End = (EditText) findViewById(R.id.Input_Mess_End);
        Refresh = (Button) findViewById(R.id.refresh);
        Spinner_City = (Spinner) findViewById(R.id.spinner_city);
        ViewGroup tableTitle = (ViewGroup) findViewById(R.id.table_title);
        tableTitle.setBackgroundColor(Color.parseColor("#B4B3B3"));
        fresh();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //选择下拉框

        adapter =ArrayAdapter.createFromResource(this, R.array.city, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner_City.setAdapter(adapter);
        Spinner_City.setVisibility(View.VISIBLE);
    class SpinnerXMLSelectedListener implements OnItemSelectedListener{
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                       long arg3) {
                Input_Mess_Start.setText(adapter.getItem(arg2)+"");
                Input_Mess_Start.setSelection(Input_Mess_Start.getText().length());
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }

        }
        Spinner_City.setOnItemSelectedListener(new SpinnerXMLSelectedListener());//选择监听
        //刷新点击事件
        Refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fresh();
                Input_Mess_Start.setText("");
                Input_Mess_End.setText("");
            }
        });
        //添加开始收费点击事件
        Start_Money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input_mess_start = Input_Mess_Start.getText().toString();
                //Log.d("input_mess_start", Input_Mess_Start.getText().toString());
                if (input_mess_start.length() == 0) {
                    Toast.makeText(MainActivity.this, "请输入车牌号", Toast.LENGTH_SHORT).show();
                } else {
                    Date curDate = new Date(System.currentTimeMillis());
                    ToDay_Trans add = new ToDay_Trans(Input_Mess_Start.getText().toString(),
                            formatter_date.format(curDate), formatter_Time.format(curDate), "", "", "", "0");//0表示还未收费的
                    add.save();
                    //Log.d("aaaaa",add.getCar_Num());
                    fresh();

                    Input_Mess_Start.setText("");
                }
            }
        });
        //实现收费输入框动态搜索
        Input_Mess_End.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
                // TODO Auto-generated method stub
                //这个应该是在改变的时候会做的动作吧，具体还没用到过。
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
                //这是文本框改变之前会执行的动作
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
/*                *这是文本框改变之后 会执行的动作
                 * 因为我们要做的就是，在文本框改变的同时，我们的listview的数据也进行相应的变动，并且如一的显示在界面上。
                 * 所以这里我们就需要加上数据的修改的动作了。*/

                selfresh(Input_Mess_End.getText().toString());//按照车牌号动态查询
            }
        });

        //结束计费
        End_Money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input_mess_end = Input_Mess_End.getText().toString();
                list = sreachbyid(get_id);
                Date curDate = new Date(System.currentTimeMillis());
                if (input_mess_end.length() == 0) {
                    Toast.makeText(MainActivity.this, "请输入或者选择车牌号", Toast.LENGTH_SHORT).show();
                } else {
                    //弹出通知框
                    AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                    dialog.setTitle("本次收费小票");
                    String start = list.get(0).getStart_Date() + " " + list.get(0).getStart_Time();
                    String end = formatter_date.format(curDate) + " " + formatter_Time.format(curDate);
                    IntervalTime intervaltime = new IntervalTime();
                    String interval = intervaltime.get_IntervalTime(start, end);
                    list.get(0).getCar_Num();
                    dialog.setMessage("\n\n车牌号码：" + list.get(0).getCar_Num() + "\n\n" + "开始时间：" + start
                            + "\n\n" + "结束时间：" + end + "\n\n" + "持续时间：" + interval);
                    dialog.setCancelable(false);
                    dialog.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });
                    dialog.show();
                    fresh();
                    Input_Mess_End.setText("");
                    //写入数据库
/*                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
                    Date curDate = new Date(System.currentTimeMillis());
                    ToDay_Trans update = new ToDay_Trans();

                    ToDay_Trans add = new ToDay_Trans(Input_Mess_End.getText().toString(),
                            formatter.format(curDate), "", "");
                    add.save();*/
                    //Log.d("aaaaa",add.getCar_Num());

                }
            }
        });
        //添加列表点击事件
        tableListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ToDay_Trans today = list.get(position);
                Input_Mess_End.setText(today.getCar_Num());
                get_id = today.getId();
                //Toast.makeText(MainActivity.this, today.getCar_Num(), Toast.LENGTH_SHORT).show();
            }
        });
        //列表长按事件
        tableListView.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {

            @Override
            public void onCreateContextMenu(ContextMenu menu, View v,
                                            ContextMenu.ContextMenuInfo menuInfo) {
                menu.add(0, 1, 0, "删除");
            }

        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem menu) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menu.getMenuInfo();
        ToDay_Trans today = list.get(info.position);
        try {
            switch (menu.getItemId()) {
                case 0:
                    Toast.makeText(MainActivity.this, today.getId() + "", Toast.LENGTH_LONG).show();
                    break;
                case 1:
                    delfresh(today.getId());
                    Toast.makeText(MainActivity.this, "删除", Toast.LENGTH_LONG).show();
                    break;
            }
        } catch (Exception e) {
        }
        return super.onContextItemSelected(menu);

    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        return false;
    }


}

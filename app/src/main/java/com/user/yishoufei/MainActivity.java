package com.user.yishoufei;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

import static android.R.attr.format;

public class MainActivity extends AppCompatActivity {

    private View homeview;
    private View web_ui;
    private WebView webView;
    private Button Start_Money;
    private EditText Input_Mess_Start;
    private EditText Input_Mess_End;
    public List<ToDay_Trans> list;

    //刷新列表
    public List<ToDay_Trans> fresh() {
        list = DataSupport.order("Start_Time desc").find(ToDay_Trans.class);
        return list;
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
        Input_Mess_Start = (EditText) findViewById(R.id.Input_Mess_Start);
        Input_Mess_End = (EditText) findViewById(R.id.Input_Mess_End);
        ViewGroup tableTitle = (ViewGroup) findViewById(R.id.table_title);
        tableTitle.setBackgroundColor(Color.rgb(177, 173, 172));
        ListView tableListView = (ListView) findViewById(R.id.list);
        TableAdapter adapter = new TableAdapter(MainActivity.this, fresh());
        tableListView.setAdapter(adapter);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        //添加开始收费点击事件
        Start_Money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input_mess_start = Input_Mess_Start.getText().toString();
                Log.d("input_mess_start",Input_Mess_Start.getText().toString());
                if (input_mess_start.length()==0) {
                    Toast.makeText(MainActivity.this, "请输入车牌号", Toast.LENGTH_SHORT).show();
                } else {
                    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss");
                    Date curDate = new Date(System.currentTimeMillis());

                    ToDay_Trans add = new ToDay_Trans(Input_Mess_Start.getText().toString(),
                            formatter.format(curDate), "", "");
                    add.save();
                    //Log.d("aaaaa",add.getCar_Num());
                    List<ToDay_Trans> list = fresh();
                    ListView tableListView = (ListView) findViewById(R.id.list);
                    TableAdapter adapter = new TableAdapter(MainActivity.this, list);
                    tableListView.setAdapter(adapter);
                    Input_Mess_Start.setText("");
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
                //Toast.makeText(MainActivity.this, today.getCar_Num(), Toast.LENGTH_SHORT).show();
            }
        });


        //设置表格标题的背景颜色
//        List<Goods> list = new ArrayList<Goods>();

//        list.add(new Goods("01", "浙C.R5180", "12:12",13,23,23));
//        list.add(new Goods("02", "鱼翅", "31312323223",34,23,23));
//        list.add(new Goods("03", "农夫山泉", "12",34,23,23));
//        list.add(new Goods("04", "飞天茅台0", "12333435445",34,23,23));
//        list.add(new Goods("05", "农家小菜", "34523",34,23,23));
//        list.add(new Goods("06", "飞天消费菜", "345456",34,23,23));
//        list.add(new Goods("07", "旺仔小牛奶", "2344",34,23,23));
//        list.add(new Goods("08", "旺旺", "23445",34,23,23));
//        list.add(new Goods("09", "达利园超时牛奶", "3234345",34,23,23));
//        list.add(new Goods("09", "达利园超时牛奶", "3234345",34,23,23));
//        list.add(new Goods("09", "达利园超时牛奶", "3234345",34,23,23));
//        list.add(new Goods("09", "达利园超时牛奶", "3234345",34,23,23));
//        list.add(new Goods("09", "达利园超时牛奶", "3234345",34,23,23));
//        list.add(new Goods("09", "达利园超时牛奶", "3234345",34,23,23));
//        list.add(new Goods("09", "达利奶====", "3234345",34,23,23));


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

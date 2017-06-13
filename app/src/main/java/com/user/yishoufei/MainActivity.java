package com.user.yishoufei;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.litepal.tablemanager.Connector;

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
    private EditText input_message;

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
        Connector.getDatabase();
        ActionBar actionBar = getSupportActionBar();//隐藏默认的控件
        if (actionBar !=null){
            actionBar.hide();
        }

        homeview = findViewById(R.id.include_home);
        web_ui =findViewById(R.id.include_view);
        webView=(WebView) findViewById(R.id.web_view);
        Start_Money =(Button) findViewById(R.id.Start_Money);
        input_message=(EditText)findViewById(R.id.input_message) ;
        ViewGroup tableTitle = (ViewGroup) findViewById(R.id.table_title);
        tableTitle.setBackgroundColor(Color.rgb(177, 173, 172));
        Start_Money.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick (View v){
                SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
                ToDay_Trans toDay_trans = new ToDay_Trans(input_message.getText().toString(),new Date(),new Date(),11);
            }
        });
        //设置表格标题的背景颜色
        List<Goods> list = new ArrayList<Goods>();
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

        ListView tableListView = (ListView) findViewById(R.id.list);

        TableAdapter adapter = new TableAdapter(MainActivity.this, list);

        tableListView.setAdapter(adapter);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event){
        if((keyCode == KeyEvent.KEYCODE_BACK)&&webView.canGoBack()){
            webView.goBack();
            return true;
        }
        return false;
    }

}

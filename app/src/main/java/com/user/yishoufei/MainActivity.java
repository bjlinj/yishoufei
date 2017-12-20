package com.user.yishoufei;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private View homeview;
    //private View web_ui;
    private View select;
    private WebView webView;
    private Button Start_Money;
    private EditText Input_Mess_Start;
    private EditText Input_Mess_End;
    private List<ToDay_Trans> list;
    private ListView tableListView;
    private long get_id;
    private Button Button_Fresh;
    private Spinner Spinner_City;
    private ArrayAdapter adapter_City;
    private Spinner Spinner_Alphabet_city;
    private ArrayAdapter adapter_Alphabet_city;
    private String city_shot_alphabet;//字幕简写
    private String city_shot;//中文简写
    private TextView Car_Num;
    private TextView Start_Time;
    private View config;
    private EditText Config_Price;
    private Button   Set_Button;
    private String pay;
    private TabHost tabHost;
    SimpleDateFormat formatter_date = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat formatter_Time = new SimpleDateFormat("HH:mm:ss");
    String currentdate= new IntervalTime().getCurrentdate();//获取当前时间

    //获取交易信息
    public List getlist(){

        List<ToDay_Trans> trans_lists = DataSupport.findAll(ToDay_Trans.class);
        return trans_lists;
    }


    //刷新列表
    public void fresh() {
        list = DataSupport.where("Type_Cord = ?", "0").find(ToDay_Trans.class);
        tableListView = (ListView) findViewById(R.id.list);
        //TodayAdapter adapter = new TodayAdapter(MainActivity.this,R.layout.list_item, list);
        //Collections.sort(list,new SortComparator());
//        for(ToDay_Trans attribute : list) {
//            System.out.println(attribute.getId());
//        }
        TableAdapter adapter = new TableAdapter(MainActivity.this, list);

        tableListView.setAdapter(adapter);
        Input_Mess_Start.setText("");
        Input_Mess_End.setText("");
        Input_Mess_Start.setText(city_shot + city_shot_alphabet);//刷新后添加简写
        Input_Mess_Start.setSelection(Input_Mess_Start.getText().length());
    }


    //按条件模糊查询
    public void selfresh(String s) {
        list.clear();
        list = DataSupport.order("Start_Date desc").order("Start_Time desc").where("Car_Num like ? and Type_Cord = ?", "%" + s + "%" ,"0").find(ToDay_Trans.class);
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
                //主页
                case R.id.navigation_home:
                    //home_button.setText(R.string.title_home);
                    homeview.setVisibility(View.VISIBLE);
                    //web_ui.setVisibility(View.GONE);
                    config.setVisibility(View.GONE);
                    return true;
/*                case R.id.navigation_dashboard:
                    homeview.setVisibility(View.GONE);
                    web_ui.setVisibility(View.VISIBLE);
                    config.setVisibility(View.GONE);
                    webView.getSettings().setJavaScriptEnabled(true);
                    webView.setWebViewClient(new WebViewClient());
                    webView.loadUrl("http://news.sina.com.cn/");
                    return true;*/
                //查询页
                case R.id.navigation_dashboard:
                    homeview.setVisibility(View.GONE);
                    //web_ui.setVisibility(View.VISIBLE);
                   // select.setVisibility(View.VISIBLE);
                    config.setVisibility(View.GONE);
                    return true;
                //设置页
                case R.id.navigation_notifications:
                    // mTextMessage.setText(R.string.title_notifications);
                    homeview.setVisibility(View.GONE);
                    //web_ui.setVisibility(View.GONE);
                    config.setVisibility(View.VISIBLE);
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
        final ActionBar actionBar = getSupportActionBar();//隐藏默认的控件
        if (actionBar != null) {
            actionBar.hide();
    }


        homeview = findViewById(R.id.include_home);//加载主页
        //web_ui = findViewById(R.id.include_view);//加载网页
        //select = findViewById(R.id.include_select);//加载查询页
        webView = (WebView) findViewById(R.id.web_view);//加载设置页
        Start_Money = (Button) findViewById(R.id.Button_Start);
        Button_Fresh = (Button) findViewById(R.id.Button_fresh);
        Input_Mess_Start = (EditText) findViewById(R.id.Input_Mess_Start);
        Input_Mess_End = (EditText) findViewById(R.id.Input_Mess_End);
        Spinner_City = (Spinner) findViewById(R.id.spinner_city);
        Spinner_Alphabet_city = (Spinner) findViewById(R.id.alphabet_city);
        Car_Num=(TextView)findViewById(R.id.car_num);
        Start_Time=(TextView)findViewById(R.id.start_time);
        ViewGroup tableTitle = (ViewGroup) findViewById(R.id.table_title);
        config=findViewById(R.id.config);
        Config_Price =(EditText) findViewById(R.id.price_edit);
        Set_Button = (Button) findViewById(R.id.set_price);

        TabHost tab = (FragmentTabHost) findViewById(android.R.id.tabhost);  //获取tabHost对象
//        tab.setup();//初始化TabHost组件
//        LayoutInflater inflater= LayoutInflater.from(this);//声明并实例化一个LayoutInflater对象
//        //关于LayoutInflater详细，请看我的另外一篇转载的总结
//        inflater.inflate(R.layout.history, tabHost.getTabContentView());
//        tabHost.addTab(tabHost.newTabSpec("tab01")
//                .setIndicator("标签页一")
//                .setContent(R.id.linearLayout1));//添加第一个标签页



        tableTitle.setBackgroundColor(Color.parseColor("#B4B3B3"));
        fresh();//初始化加载刷新数据库
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //选择下拉框
        adapter_City = ArrayAdapter.createFromResource(this, R.array.city, android.R.layout.simple_spinner_item);
        adapter_City.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner_City.setAdapter(adapter_City);
        Spinner_City.setVisibility(View.VISIBLE);
        adapter_Alphabet_city = ArrayAdapter.createFromResource(this, R.array.alphabet_city, android.R.layout.simple_spinner_item);
        adapter_Alphabet_city.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner_Alphabet_city.setAdapter(adapter_Alphabet_city);
        Spinner_Alphabet_city.setVisibility(View.VISIBLE);
        class SpinnerXMLSelectedListener implements OnItemSelectedListener {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                       long arg3) {
                Input_Mess_Start.setText(adapter_City.getItem(arg2) + "" + city_shot_alphabet);//先设置简写加上老的字母简写
                city_shot = adapter_City.getItem(arg2) + "";//把简写付给变量
                Input_Mess_Start.setSelection(Input_Mess_Start.getText().length());//光标移到最后
            }

            public void onNothingSelected(AdapterView<?> arg0) {

            }

        }
        class SpinnerAlphabetXMLSelectedListener implements OnItemSelectedListener {
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2,
                                       long arg3) {
                Input_Mess_Start.setText("");
                Input_Mess_Start.setText(city_shot);//先添加城市简写
                Input_Mess_Start.append(adapter_Alphabet_city.getItem(arg2) + "");//再添加字母简写
                city_shot_alphabet = adapter_Alphabet_city.getItem(arg2) + "";//添加字母简写到变量
                Input_Mess_Start.setSelection(Input_Mess_Start.getText().length());
            }

            public void onNothingSelected(AdapterView<?> arg0) {


            }

        }
        Spinner_Alphabet_city.setOnItemSelectedListener(new SpinnerAlphabetXMLSelectedListener());//字母监听
        Spinner_City.setOnItemSelectedListener(new SpinnerXMLSelectedListener());//简写选择监听

        //添加开始收费点击事件
        Start_Money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input_mess_start = Input_Mess_Start.getText().toString();
                //Log.d("input_mess_start", Input_Mess_Start.getText().toString());
                if (input_mess_start.equals(city_shot + city_shot_alphabet)||input_mess_start.length()==0) {
                    Toast.makeText(MainActivity.this, "请输入车牌号", Toast.LENGTH_SHORT).show();
                } else {
                    Date curDate = new Date(System.currentTimeMillis());
                    ToDay_Trans add = new ToDay_Trans(Input_Mess_Start.getText().toString().toUpperCase(),
                            formatter_date.format(curDate), formatter_Time.format(curDate), "", "", "", "0");//0表示还未收费的
                    add.save();
                    //Log.d("aaaaa",add.getCar_Num());
                    list.clear();
                    fresh();
                    Input_Mess_Start.setText("");
                    Input_Mess_Start.setText(city_shot + city_shot_alphabet);//点击开始收费后添加简写
                    Input_Mess_Start.setSelection(Input_Mess_Start.getText().length());
                    save();//生成本地文件
                    ftpUpload();//上传服务器
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

        //刷新列表
        Button_Fresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.clear();
                fresh();
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
                list = sreachbyid(get_id);
                Date curDate = new Date(System.currentTimeMillis());
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("本次收费小票");
                String start = list.get(0).getStart_Date() + " " + list.get(0).getStart_Time();
                String end = formatter_date.format(curDate) + " " + formatter_Time.format(curDate);
                IntervalTime intervaltime = new IntervalTime();
                String interval = intervaltime.get_IntervalTime(start, end);
                long  still_minutes =intervaltime.get_still_minutes(start, end);
                //double  unitprice=new RuleConfig().getUnitPrice();
                double unitprice= Double.parseDouble(Config_Price.getText().toString());
                DecimalFormat df = new DecimalFormat("#0.00");
                pay = df.format((unitprice/60)*still_minutes);//计算消费金额
                list.get(0).getCar_Num();
                dialog.setMessage("\n车牌号码：" + list.get(0).getCar_Num() + "\n\n" + "开始时间：" + start
                        + "\n\n" + "结束时间：" + end + "\n\n" + "持续时间：" + interval+
                        "\n\n应收费用："+pay+"元");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确定收费", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //UPDATE 数据库
                        ToDay_Trans today_trans = new ToDay_Trans();
                        Date curDate = new Date(System.currentTimeMillis());
                        today_trans.setEnd_Date(formatter_date.format(curDate));
                        today_trans.setEnd_Time(formatter_Time.format(curDate));
                        today_trans.setType_Cord("1");//1表示已结账
                        today_trans.setMoney(pay);
                        today_trans.update(get_id);
                        list.clear();
                        fresh();
                        save();//生成本地文件
                        ftpUpload();//上传服务器
                    }
                });
                dialog.setNeutralButton("取消退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        list.clear();
                        fresh();
                    }
                });

                dialog.show();
                Input_Mess_End.setText("");

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
        //设置收费单价
        //数据库获取收费单价
        Intent intent = getIntent();
        Config_Price.setText(intent.getStringExtra("confi_data"));

        Set_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RuleConfig ruleconfig =  new RuleConfig();
                String config_str  = Config_Price.getText().toString();
                //Log.d("config_str",config_str+"======");
                if(config_str==null || config_str.equals("")){
                   Toast.makeText(MainActivity.this, "请输入数值", Toast.LENGTH_SHORT).show();
               }else {
                    ruleconfig.setUnitPrice(Double.parseDouble(Config_Price.getText().toString()));
                    ruleconfig.updateAll();//跟新新的收费单价
                    Intent Main_intent = new Intent(MainActivity.this, MainActivity.class);
                    Main_intent.putExtra("confi_data", ruleconfig.getUnitPrice() + "");
                    startActivity(Main_intent);//跳到主页
                    Toast.makeText(MainActivity.this, "修改成功返回主页", Toast.LENGTH_SHORT).show();
                }
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
                    list.clear();
                    fresh();
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

    //写文件
    public void save(){
        FileOutputStream out = null;
        BufferedWriter writer = null;
        //生成文件，写入本地
        String   model= android.os.Build.MODEL;//获取手机型号
        String carrier= android.os.Build.MANUFACTURER;//获取手机品牌
        String inputText = model+"==="+carrier+"\n";
        List<ToDay_Trans> lists=getlist();
        for (ToDay_Trans list:lists){
            inputText+=list.getId()+","+list.getCar_Num()+","+list.getStart_Date()+","+list.getStart_Time()+","+list.getEnd_Date()+","+
                    list.getEnd_Time()+","+list.getMoney()+","+list.getType_Cord()+"\n";
        }
        try{
            out = openFileOutput(currentdate, Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            writer.write(inputText);
            //Log.d("inputTest",inputText+"=1=1=1=1==1");
        }catch (IOException e){
            e.printStackTrace();
        }finally {
            try {
                if (writer !=null){
                    writer.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    //ftp上传文件
    private void ftpUpload() {

        new Thread() {
            public void run() {
                try {
                    System.out.println("正在连接ftp服务器....");
                    FTPManager ftpManager = new FTPManager();
                    Context context=MainActivity.this;
                    File file=context.getFilesDir();
                    String filepath=file+"/"+currentdate;//获取本地写入文件的路径
                    //String path=file.getAbsolutePath()+"data";
                    System.out.println("获取安卓路径=="+file+"====filepath"+filepath);
                    if (ftpManager.connect()) {
                        if (ftpManager.uploadFile(filepath, "mmcblk0/ysf/")) {
                            ftpManager.closeFTP();
                        }
                    }
                } catch (Exception e) {
                    // TODO: handle exception
                    // System.out.println(e.getMessage());
                }
            }
        }.start();
    }
}

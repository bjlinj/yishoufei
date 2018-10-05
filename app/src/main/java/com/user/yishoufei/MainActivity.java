package com.user.yishoufei;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentTabHost;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.qq.e.ads.banner.ADSize;
import com.qq.e.ads.banner.AbstractBannerADListener;
import com.qq.e.ads.banner.BannerView;
import com.qq.e.comm.util.AdError;

import org.json.JSONArray;
import org.json.JSONObject;
import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static java.lang.Boolean.FALSE;


public class MainActivity extends AppCompatActivity {

    private View homeview;
    private View search_view;
    private View select;
    private WebView webView;
    private Button Start_Money;
    private EditText Input_Mess_Start;
    private EditText Input_Mess_End;
    private List<ToDay_Trans> list;
    private List<ToDay_Trans> List_Steal;
    private List<ToDay_Trans> jsonlist;
    private List<ToDay_Trans> sreachbyidlist;
    private List<UserName> userlist;
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
    private View about_ous;
    private EditText Config_Free_Price;
    private EditText First_hour_edit;
    private EditText First_yuan_edit;
    private EditText First_min_edit;
    private EditText After_yuan_hour;
    private TextView After_hour;
    private TextView After_hour_min;
    private TextView Day_start_time; //设置白天开始时间
    private TextView Day_end_time;//设置白天结束时间

    private EditText  N_First_hour_edit;//晚上首1小时收费默认为1
    private EditText  N_First_yuan_hour_edit;//晚上首1小时收费1元/60分钟
    private EditText  N_First_min_edit;//晚上首1小时收费1元/60分钟
    private EditText  N_After_yuan_hour_edit;//晚上1小时后1元/60分钟
    private EditText N_FreePrice;//晚上设置免费分钟数
    private TextView N_After_hour;
    private TextView N_After_hour_min;
    private TextView Search_start_date;
    private TextView Search_end_date;
    private RadioGroup Search_Steal;
    private String Steal_Searched;
    private Button Get_Search;
    private ListView Search_ListView;
    private List<ToDay_Trans>  List_Steal_card ;


    private Button   Set_Button;
    private String pay;
    private TabHost tabHost;
    private List<RuleConfig> list_ruleconfig;
    private TimePicker Get_Time;


    // 获取日历的一个对象
    Calendar  calendar = Calendar.getInstance();
    private int hour = calendar.get(Calendar.HOUR);
    private int min = calendar.get(Calendar.MINUTE);
    int Year = calendar.get(Calendar.YEAR);
    int Month = calendar.get(Calendar.MONTH);
    int Day = calendar.get(Calendar.DAY_OF_MONTH);
    //String SerialNumber ;

    private String   model= android.os.Build.MODEL;//获取手机型号
    private String carrier= android.os.Build.MANUFACTURER;//获取手机品牌

    //插屏廣告
    private ViewGroup bannerContainer;
    private BannerView bv;
    private String posId;


//    private Switch sw_price_tex;//每小时X元默认开启
//    private Switch sw_limit_tex;//每天上限金额默认关闭
//    private Switch sw_free_price_tex;//免费分钟数默认关闭
//    private Switch sw_hour_tex;//x小时以内默认关闭


//    private Boolean b_price_tex=true;//每小时X元默认开启
//    private Boolean b_limit_tex=false;//每天上限金额默认关闭
//    private Boolean b_free_price_tex=false;//免费分钟数默认关闭
//    private Boolean b_less_hour_edit=false;//x小时以内默认关闭
//    private Boolean b_config_all=false;//总体设置项



    SimpleDateFormat formatter_date = new SimpleDateFormat("yyyy-MM-dd");
    SimpleDateFormat formatter_Time = new SimpleDateFormat("HH:mm:ss");
    String currentdate= new IntervalTime().getCurrentdate();//获取当前时间

    //获取交易信息
    public List getlist(){

        List<ToDay_Trans> trans_lists = DataSupport.findAll(ToDay_Trans.class);
        return trans_lists;
    }


    //刷新列表
//    public void fresh() {
//
//        list = DataSupport.where("Type_Cord = ?", "0").find(ToDay_Trans.class);
//        tableListView = (ListView) findViewById(R.id.listview);
//
//        //TodayAdapter adapter = new TodayAdapter(MainActivity.this,R.layout.list_item, list);
//        //Collections.sort(list,new SortComparator());
//       for(ToDay_Trans attribute : list) {
//           Log.d("get_card",attribute.getCar_Num());
//       }
//        TableAdapter adapter = new TableAdapter(MainActivity.this, list);
//
//        tableListView.setAdapter(adapter);
//        Input_Mess_Start.setText("");
//        //Input_Mess_End.setText("");
//        Input_Mess_Start.setText(city_shot + city_shot_alphabet);//刷新后添加简写
//        Input_Mess_Start.setSelection(Input_Mess_Start.getText().length());
//    }

    //取出用户ID
    public String getuserID(){
        //userlist = DataSupport.findAll(UserName.class);
        //Log.d("userlist=0=0=0=",userlist.get(0).getUsername());
          return   (model+"_"+carrier+"_"+getLocalVersionName(this)).replace(" ", "");
    }
//获取使用版本号名称
    public static String getLocalVersionName(Context ctx) {
        String localVersion = "";
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersion = packageInfo.versionName;
            Log.d("本软件的版本名：" ,localVersion);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersion;
    }

    //SQL查询方式
    public void querysql(){
        Cursor cursor = DataSupport.findBySQL("select * from ToDay_Trans where id>?", "0");


    }

    //按条件模糊刷新
    public void selfresh(String s) {
        list = DataSupport.order("Start_Date desc").order("Start_Time desc").where("Car_Num like ? and Type_Cord = ? and Is_Steal = ?", "%" + s + "%" ,"0","0").find(ToDay_Trans.class);
        tableListView = (ListView) findViewById(R.id.listview);
        //TodayAdapter adapter = new TodayAdapter(MainActivity.this,R.layout.list_item, list);
        TableAdapter adapter = new TableAdapter(MainActivity.this, list);
        tableListView.setAdapter(adapter);
        Input_Mess_Start.setText("");
        //Input_Mess_End.setText("");
        Input_Mess_Start.setText(city_shot + city_shot_alphabet);//刷新后添加简写
        Input_Mess_Start.setSelection(Input_Mess_Start.getText().length());

    }

    //逃票车牌查询
    public void Sel_Steal(String start_date,String end_date,String Is_Steal) throws ParseException{
        //Log.d("======1=1=",start_date+end_date+Is_Steal);
        List_Steal =DataSupport.order("Start_Date ").order("Start_Time ").where("Is_Steal like ? and Start_Date >=? and Start_Date<=?","%"+Is_Steal+"%",start_date,end_date).find(ToDay_Trans.class);
        Search_ListView=(ListView)findViewById(R.id.search_listview);

        SearchAdapter adapter = new SearchAdapter(MainActivity.this, List_Steal);
        Search_ListView.setAdapter(adapter);
//        for (ToDay_Trans rc : List_Steal){
//            Log.d("getCar_Num",rc.getCar_Num());
//        }
    }


    public List<ToDay_Trans> get_Steal_card(String card_num){
        return  DataSupport.order("Start_Date ").order("Start_Time ").where("Car_Num = ? and Is_Steal = ?  ",card_num.toUpperCase(),"1").find(ToDay_Trans.class);


    }

    //用ID精确查找
    public List<ToDay_Trans> sreachbyid(long s) {
        sreachbyidlist = DataSupport.findAll(ToDay_Trans.class, s);
        return sreachbyidlist;
    }

    //按ID删除数据
    public void delfresh(long s) {
        DataSupport.delete(ToDay_Trans.class, s);
        selfresh("");//删除之后刷新一下数据
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
                    search_view.setVisibility(View.GONE);
                    config.setVisibility(View.GONE);
                    about_ous.setVisibility(View.GONE);
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
                    about_ous.setVisibility(View.GONE);
                    search_view.setVisibility(View.VISIBLE);
                    if(Search_start_date.getText().toString()==null||Search_start_date.getText().toString().equals("")){
                        Search_start_date.setText(formatter_date.format(new Date()));
                        Search_end_date.setText(formatter_date.format(new Date()));
                    }

                    // select.setVisibility(View.VISIBLE);
                    config.setVisibility(View.GONE);
                    return true;
                //设置页
                case R.id.navigation_settings:
                    // mTextMessage.setText(R.string.title_notifications);
                    homeview.setVisibility(View.GONE);
                    search_view.setVisibility(View.GONE);
                    about_ous.setVisibility(View.GONE);
                    config.setVisibility(View.VISIBLE);
                    getConfigSet();//加载参数
                    return true;
                case R.id.navigation_notifications:
                    // mTextMessage.setText(R.string.title_notifications);
                    homeview.setVisibility(View.GONE);
                    search_view.setVisibility(View.GONE);
                    config.setVisibility(View.GONE);
                    about_ous.setVisibility(View.VISIBLE);
                    getConfigSet();//加载参数
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
//插屏广告----
        bannerContainer = (ViewGroup) this.findViewById(R.id.bannerContainer);
        this.getBanner().loadAD();
//插屏广告----


        final ActionBar actionBar = getSupportActionBar();//隐藏默认的控件
        if (actionBar != null) {
            actionBar.hide();
        }

        // SerialNumber = Installation.id(getApplicationContext());//获取全局ID
      // final String SerialNumber = android.os.Build.SERIAL;
      //  GetPrimaryId.getIMEI(getApplication());
      //  Log.d("SerialNumber=======",GetPrimaryId.getIMEI(getApplication())+"===");
        homeview = findViewById(R.id.include_home);//加载主页
        search_view = findViewById(R.id.include_search);//加载搜索页
        //select = findViewById(R.id.include_select);//加载查询页
        webView = (WebView) findViewById(R.id.web_view);//加载设置页
        about_ous = findViewById(R.id.about_ous);//加载关于页
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
        //Config_Price =(EditText) findViewById(R.id.price_edit);
        Config_Free_Price=(EditText)findViewById(R.id.free_price_edit);
        //获取焦点   https://blog.csdn.net/aaawqqq/article/details/50259713
         First_hour_edit=(EditText)findViewById(R.id.first_hour_edit);
        First_yuan_edit=(EditText)findViewById(R.id.first_yuan_edit);
        First_min_edit=(EditText)findViewById(R.id.first_min_edit);
        After_yuan_hour=(EditText)findViewById(R.id.after_yuan_edit);
        After_hour=(TextView)findViewById(R.id.after_hour);
        After_hour_min=(TextView)findViewById(R.id.after_hour_min);
        Get_Time=(TimePicker) findViewById(R.id.tpPicker);
        Day_start_time=(TextView)findViewById(R.id.day_start_date_fee);//设置白天开始时间
        Day_end_time=(TextView)findViewById(R.id.day_end_date_fee);//设置白天结束时间
        N_After_hour=(TextView)findViewById(R.id.n_after_hour);
        N_After_hour_min=(TextView)findViewById(R.id.n_after_hour_min);
        N_First_hour_edit=(EditText)findViewById(R.id.n_first_hour_edit);
        N_First_hour_edit.setFocusable(true);//获取焦点
        N_First_hour_edit.setFocusableInTouchMode(true);//获取焦点
        N_First_yuan_hour_edit=(EditText)findViewById(R.id.n_first_yuan_edit);
        N_First_min_edit=(EditText)findViewById(R.id.n_first_min_edit);
        N_First_min_edit.setFocusable(true);//获取焦点
        N_First_min_edit.setFocusableInTouchMode(true);//获取焦点
        N_After_yuan_hour_edit=(EditText)findViewById(R.id.n_after_yuan_edit);
        N_FreePrice=(EditText)findViewById(R.id.n_free_price_edit);
        Search_start_date=(TextView)findViewById(R.id.search_start_date) ;
        Search_end_date=(TextView)findViewById(R.id.search_end_date) ;
        Search_Steal=(RadioGroup)findViewById(R.id.radioGroup);
        Get_Search=(Button)findViewById(R.id.getsearch);




        Set_Button = (Button) findViewById(R.id.set_price);
//        sw_price_tex =(Switch)findViewById(R.id.sw_price_tex);//单价每小时设置，默认为开
//        sw_limit_tex=(Switch)findViewById(R.id.sw_limit_tex);//每天上限金额默认关闭
//        sw_free_price_tex=(Switch)findViewById(R.id.sw_free_price_tex);//免费分钟数默认关闭
//        sw_hour_tex=(Switch)findViewById(R.id.sw_hour_tex);//X小时以内X元，之后每小时X元

        TabHost tab = (FragmentTabHost) findViewById(android.R.id.tabhost);  //获取tabHost对象
//        tab.setup();//初始化TabHost组件
//        LayoutInflater inflater= LayoutInflater.from(this);//声明并实例化一个LayoutInflater对象
//        //关于LayoutInflater详细，请看我的另外一篇转载的总结
//        inflater.inflate(R.layout.history, tabHost.getTabContentView());
//        tabHost.addTab(tabHost.newTabSpec("tab01")
//                .setIndicator("标签页一")
//                .setContent(R.id.linearLayout1));//添加第一个标签页




        tableTitle.setBackgroundColor(Color.parseColor("#B4B3B3"));
        selfresh("");//初始化加载刷新数据库
        getConfigSet();//加载参数
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


//
//        //是否开启单位每小时
//        sw_price_tex.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView,
//                                         boolean isChecked) {
//                // TODO Auto-generated method stub
//                if (isChecked) {
//                    b_price_tex=true;
//                    b_less_hour_edit=false;
//                    Toast.makeText(MainActivity.this, "开启", Toast.LENGTH_SHORT).show();
//                    sw_hour_tex.setChecked(b_less_hour_edit);//X小时以内X元，之后每小时X元 关闭
//                } else {
//                    b_price_tex=false;
//                    Toast.makeText(MainActivity.this, "关闭", Toast.LENGTH_SHORT).show();
//
//                }
//            }
//        });
//
//        //免费分钟数默认关闭
//        sw_free_price_tex.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView,
//                                         boolean isChecked) {
//                // TODO Auto-generated method stub
//                if (isChecked) {
//                    Toast.makeText(MainActivity.this, "开启", Toast.LENGTH_SHORT).show();
//                    b_free_price_tex=true;
//                    b_less_hour_edit=false;
//                    sw_hour_tex.setChecked(b_less_hour_edit);//X小时以内X元，之后每小时X元 关闭
//                } else {
//                    b_free_price_tex=false;
//                    Toast.makeText(MainActivity.this, "关闭", Toast.LENGTH_SHORT).show();
//
//                }
//            }
//        });
//
//        //每天上限金额默认关闭
//        sw_limit_tex.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView,
//                                         boolean isChecked) {
//                // TODO Auto-generated method stub
//                if (isChecked) {
//                    Toast.makeText(MainActivity.this, "开启", Toast.LENGTH_SHORT).show();
//                    b_limit_tex=true;
//                } else {
//                    b_limit_tex=false;
//                    Toast.makeText(MainActivity.this, "关闭", Toast.LENGTH_SHORT).show();
//
//                }
//            }
//        });
//        //x小时以内默认关闭
//        sw_hour_tex.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView,
//                                         boolean isChecked) {
//                // TODO Auto-generated method stub
//                if (isChecked) {
//                    Toast.makeText(MainActivity.this, "开启", Toast.LENGTH_SHORT).show();
//                    b_less_hour_edit=true;
//                    b_limit_tex=false;
//                    b_free_price_tex=false;
//                    sw_price_tex.setChecked(b_limit_tex);//X小时以内X元，之后每小时X元 关闭
//                    sw_free_price_tex.setChecked(b_free_price_tex);//免费分钟数关闭
//                } else {
//                    b_less_hour_edit=false;
//                    Toast.makeText(MainActivity.this, "关闭", Toast.LENGTH_SHORT).show();
//
//                }
//            }
//        });
//
//        // 初始化并且设置TimePicker
//        Get_Time.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
//            @Override
//            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
//                setTitle(hourOfDay + ":" + minute);
//                Get_Time.setIs24HourView(true);
//            }
//        });
//        // 时间对话框
//        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
//            @Override
//            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                setTitle(hourOfDay + ":" + minute);
//            }
//        }, hour, min, true).show();

        //设置白天开始时间
        Day_start_time.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        setTitle(hourOfDay + ":" + minute);
                        Day_start_time.setText(String.format("%02d", hourOfDay)+ ":" + String.format("%02d", minute));
                    }
                }, hour, min, true).show();
            }
        });
        //设置白天结束时间
        Day_end_time.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                new TimePickerDialog(MainActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        setTitle(hourOfDay + ":" + minute);
                        Day_end_time.setText(String.format("%02d", hourOfDay)+ ":" + String.format("%02d", minute));
                       // Log.d("Day_end_time",(String.format("%02d", hourOfDay)+ ":" + String.format("%02d", minute)));
                    }
                }, hour, min, true).show();
            }
        });
        //添加开始收费点击事件
        Start_Money.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input_mess_start = Input_Mess_Start.getText().toString();
                //Log.d("input_mess_start", Input_Mess_Start.getText().toString());
                if (input_mess_start.equals(city_shot + city_shot_alphabet)||input_mess_start.length()==0) {
                    Toast.makeText(MainActivity.this, "请输入车牌号", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        Date curDate = new Date(System.currentTimeMillis());
                        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
                        String now = sdf.format(new Date());
                        B_isset_Right_time b_isset_Right_time = new B_isset_Right_time();
                        Boolean is_day_time = b_isset_Right_time.is_day_time(now, Day_start_time.getText() + "", Day_end_time.getText() + "");
                        List_Steal_card = get_Steal_card(input_mess_start);

                        //判断最近是否有逃票行为
                        if (List_Steal_card.size()==0 || List_Steal_card==null) {

                        }else{
                            Log.d("List_Steal_card",List_Steal_card.get(0).getCar_Num());
                            AlertDialog.Builder dialog_steal = new AlertDialog.Builder(MainActivity.this);
                            String start = List_Steal_card.get(0).getStart_Date() + " " + List_Steal_card.get(0).getStart_Time();
                            String end =  List_Steal_card.get(0).getEnd_Date()+ " " + List_Steal_card.get(0).getEnd_Time();
                            IntervalTime intervaltime = new IntervalTime();
                            String interval = intervaltime.get_IntervalTime(start, end);
                            long  still_minutes =intervaltime.get_still_minutes(start, end);
                            dialog_steal.setTitle("最近一次逃票");
                            dialog_steal.setMessage("\n车牌号码：" + List_Steal_card.get(0).getCar_Num() + "\n\n" + "开始时间：" + List_Steal_card.get(0).getStart_Date()+" "+List_Steal_card.get(0).getStart_Time()
                                    + "\n\n" + "结束时间：" + List_Steal_card.get(0).getEnd_Date()+" "+ List_Steal_card.get(0).getEnd_Time()+ "\n\n" + "持续时间：" + interval+
                                    "\n\n应收费用："+List_Steal_card.get(0).getMoney()+"元");

                            dialog_steal.setPositiveButton("确认补缴", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //UPDATE 数据库
                                    ToDay_Trans today_trans = new ToDay_Trans();
                                    today_trans.setIs_Steal("0");//非逃票
                                    today_trans.setType_Cord("1");//已经结清
                                    today_trans.update(List_Steal_card.get(0).getId());
                                    // Log.d("get_id=2=1=2=1",get_id+"");
                                    Toast.makeText(MainActivity.this, "补缴成功", Toast.LENGTH_SHORT).show();

                                }
                            });

                            dialog_steal.setNeutralButton("还未补缴", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(MainActivity.this, "下次继续提醒", Toast.LENGTH_SHORT).show();
                                }
                            });
                            dialog_steal.show();

                        }

                            //Log.d("传输时间",now+Day_start_time.getText()+""+Day_end_time.getText()+"");
                            //Log.d("结果",is_day_time+"");
                            String is_day_flay;
                            if (is_day_time) {
                                is_day_flay = "1";
                            } else {
                                is_day_flay = "0";
                            }
                            ToDay_Trans add = new ToDay_Trans(Input_Mess_Start.getText().toString().toUpperCase(),
                                    formatter_date.format(curDate), formatter_Time.format(curDate), "", "", "", "0", getuserID(), is_day_flay, "0");//0表示还未收费的
                            add.save();
                            //Log.d("aaaaa",SerialNumber);
                            list.clear();
                            selfresh("");
                            Input_Mess_Start.setText("");
                            Input_Mess_Start.setText(city_shot + city_shot_alphabet);//点击开始收费后添加简写
                            Input_Mess_Start.setSelection(Input_Mess_Start.getText().length());
                            save();//生成本地文件
                            //ftpUpload();//上传服务器
                            httpJson(add, "getjson");//以json串的形式传到后台


                            // Save_mysql Sl= new Save_mysql();
                            // Sl.insert(add);

                    }

                    catch (ParseException e) {
                        e.printStackTrace();
                    }
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
                selfresh("");
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
                sreachbyidlist = sreachbyid(get_id);
                Date curDate = new Date(System.currentTimeMillis());
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                String start = sreachbyidlist.get(0).getStart_Date() + " " + sreachbyidlist.get(0).getStart_Time();
                String end = formatter_date.format(curDate) + " " + formatter_Time.format(curDate);
                IntervalTime intervaltime = new IntervalTime();
                String interval = intervaltime.get_IntervalTime(start, end);
                long  still_minutes =intervaltime.get_still_minutes(start, end);
                //double  unitprice=new RuleConfig().getUnitPrice();
               // double unitprice= Double.parseDouble(Config_Price.getText().toString());
                double freeprice=Double.parseDouble(Config_Free_Price.getText().toString());
                double first_hour_edit = Double.parseDouble(First_hour_edit.getText().toString());
                double first_yuan_edit=Double.parseDouble(First_yuan_edit.getText().toString());
                double first_min_edit=Double.parseDouble(First_min_edit.getText().toString());
                double after_yuan_edit=Double.parseDouble(After_yuan_hour.getText().toString());
                double n_free_price_edit=Double.parseDouble(N_FreePrice.getText().toString());
                double n_first_hour_edit=Double.parseDouble(N_First_hour_edit.getText().toString());
                double n_first_yuan_edit=Double.parseDouble(N_First_yuan_hour_edit.getText().toString());
                double n_first_min_edit=Double.parseDouble(N_First_min_edit.getText().toString());
                double n_after_yuan_edit=Double.parseDouble(N_After_yuan_hour_edit.getText().toString());

                DecimalFormat df = new DecimalFormat("#0.00");
                //Log.d("白天还是晚上",sreachbyidlist.get(0).getIs_day());
                //白天收费
                if(sreachbyidlist.get(0).getIs_day()==null||sreachbyidlist.get(0).getIs_day().equals("1")){
                    dialog.setTitle("白天收费小票");
                if(freeprice>=still_minutes){
                    //Log.d("freeprice====",freeprice+"");
                    //Log.d("still_minutes====",still_minutes+"");
                    pay = df.format(0.00);//计算消费金额
                    //pay = df.format((unitprice/60)*still_minutes);//计算消费金额
                    sreachbyidlist.get(0).getCar_Num();
                    dialog.setMessage("\n车牌号码：" + list.get(0).getCar_Num() + "\n\n" + "开始时间：" + start
                            + "\n\n" + "结束时间：" + end + "\n\n" + "持续时间：" + interval+
                            "\n\n免费分钟数为:"+freeprice+"分钟\n\n应收费用："+pay+"元");
                    dialog.setCancelable(false);
                }else{
                    //pay = df.format((unitprice)/60*(still_minutes-freeprice));//计算消费金额

                    if((still_minutes-freeprice)<first_hour_edit*60){

                        //pay=df.format((still_minutes-freeprice)*first_yuan_edit/first_min_edit);
                        //(持续时间-免费时间)/单位分钟数*单位收费金额  有小数点的直接进一位
                        pay=df.format(Math.ceil((still_minutes-freeprice)/first_min_edit)*first_yuan_edit);
                        sreachbyidlist.get(0).getCar_Num();
                        dialog.setMessage("\n车牌号码：" + list.get(0).getCar_Num() + "\n\n" + "开始时间：" + start
                                + "\n\n" + "结束时间：" + end + "\n\n" + "持续时间：" + interval+
                                "\n\n免费分钟数为: "+freeprice+"分钟\n\n应收费用："+pay+"元");
                        dialog.setCancelable(false);
                    }else{
                        //pay= df.format((first_yuan_edit/first_min_edit)*first_hour_edit*60+((still_minutes-freeprice)-first_hour_edit*60)*after_yuan_edit/first_min_edit);
                        //
                        pay= df.format((first_yuan_edit/first_min_edit)*first_hour_edit*60+Math.ceil(((still_minutes-freeprice)-first_hour_edit*60)/first_min_edit)*after_yuan_edit);
//                        Log.d("1===first_yuan_edit:",first_yuan_edit+"");
//                        Log.d("1===first_min_edit:",first_min_edit+"");
//                        Log.d("1===first_hour_edit:",first_hour_edit+"");
//                        Log.d("1===still_minutes:",still_minutes+"");
//                        Log.d("1===freeprice:",freeprice+"");
//                        Log.d("1===first_hour_edit:",first_hour_edit+"");
//                        Log.d("1===after_yuan_edit:",after_yuan_edit+"");
                       sreachbyidlist.get(0).getCar_Num();
                        dialog.setMessage("\n车牌号码：" + list.get(0).getCar_Num() + "\n\n" + "开始时间：" + start
                                + "\n\n" + "结束时间：" + end + "\n\n" + "持续时间：" + interval+
                                "\n\n免费分钟数为: "+freeprice+"分钟\n\n应收费用："+pay+"元");
                        dialog.setCancelable(false);

                    }

                }
                }
                //晚上收费
                else{
                    dialog.setTitle("夜间收费小票");
                    if(n_free_price_edit>=still_minutes){
                        //Log.d("freeprice====",freeprice+"");
                        //Log.d("still_minutes====",still_minutes+"");
                        pay = df.format(0.00);//计算消费金额
                        //pay = df.format((unitprice/60)*still_minutes);//计算消费金额
                        sreachbyidlist.get(0).getCar_Num();
                        dialog.setMessage("\n车牌号码：" + list.get(0).getCar_Num() + "\n\n" + "开始时间：" + start
                                + "\n\n" + "结束时间：" + end + "\n\n" + "持续时间：" + interval+
                                "\n\n免费分钟数为:"+freeprice+"分钟\n\n应收费用："+pay+"元");
                        dialog.setCancelable(false);
                    }else{
                        //pay = df.format((unitprice)/60*(still_minutes-freeprice));//计算消费金额

                        if((still_minutes-n_free_price_edit)<n_first_hour_edit*60){
                            //pay=df.format((still_minutes-n_free_price_edit)*n_first_yuan_edit/n_first_min_edit);
                            pay=df.format(Math.ceil((still_minutes-n_free_price_edit)/n_first_min_edit)*n_first_yuan_edit);

                            sreachbyidlist.get(0).getCar_Num();
                            dialog.setMessage("\n车牌号码：" + list.get(0).getCar_Num() + "\n\n" + "开始时间：" + start
                                    + "\n\n" + "结束时间：" + end + "\n\n" + "持续时间：" + interval+
                                    "\n\n免费分钟数为: "+freeprice+"分钟\n\n应收费用："+pay+"元");
                            dialog.setCancelable(false);
                        }else{
                            pay= df.format((n_first_yuan_edit/n_first_min_edit)*n_first_hour_edit*60+Math.ceil(((still_minutes-n_free_price_edit)-n_first_hour_edit*60)/n_first_min_edit)*n_after_yuan_edit);
                            Log.d("===n_first_yuan_edit:",n_first_yuan_edit+"");
                            Log.d("===n_first_min_edit:",n_first_min_edit+"");
                            Log.d("===first_hour_edit:",n_first_hour_edit+"");
                            Log.d("===still_minutes:",still_minutes+"");
                            Log.d("===n_free_price_edit:",n_free_price_edit+"");
                            Log.d("===n_first_hour_edit:",n_first_hour_edit+"");
                            Log.d("===n_after_yuan_edit:",n_after_yuan_edit+"");
                            sreachbyidlist.get(0).getCar_Num();
                            dialog.setMessage("\n车牌号码：" + list.get(0).getCar_Num() + "\n\n" + "开始时间：" + start
                                    + "\n\n" + "结束时间：" + end + "\n\n" + "持续时间：" + interval+
                                    "\n\n免费分钟数为: "+n_free_price_edit+"分钟\n\n应收费用："+pay+"元");
                            dialog.setCancelable(false);

                        }

                    }

                }


                dialog.setPositiveButton("确定收费", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //UPDATE 数据库
                        ToDay_Trans today_trans = new ToDay_Trans();
                        Date curDate = new Date(System.currentTimeMillis());
                        today_trans.setEnd_Date(formatter_date.format(curDate));
                        today_trans.setEnd_Time(formatter_Time.format(curDate));
                        today_trans.setType_Cord("1");//1表示已结账
                        today_trans.setIs_Steal("0");//非逃票
                        today_trans.setMoney(pay);
                        today_trans.update(get_id);
                       // Log.d("get_id=2=1=2=1",get_id+"");
                        sreachbyidlist.clear();
                        selfresh("");
                        Toast.makeText(MainActivity.this, "收费成功", Toast.LENGTH_SHORT).show();
                        save();//生成本地文件
                        //ftpUpload();//上传服务器
                        jsonlist = sreachbyid(get_id);
                        httpJson(jsonlist.get(0),"getjson");//把最新数据传到后台

                    }
                });
                dialog.setNeutralButton("取消退出", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        list.clear();
                        selfresh("");
                    }
                });
                dialog.setNegativeButton("确认逃票", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ToDay_Trans today_trans = new ToDay_Trans();
                        Date curDate = new Date(System.currentTimeMillis());
                        today_trans.setEnd_Date(formatter_date.format(curDate));
                        today_trans.setEnd_Time(formatter_Time.format(curDate));
                        today_trans.setType_Cord("0");//0表示已未结账
                        today_trans.setIs_Steal("1");//逃票
                        today_trans.setMoney(pay);
                        today_trans.update(get_id);
                        // Log.d("get_id=2=1=2=1",get_id+"");
                        sreachbyidlist.clear();
                        selfresh("");
                        Toast.makeText(MainActivity.this, "逃票确认成功", Toast.LENGTH_SHORT).show();
                        save();//生成本地文件
                        //ftpUpload();//上传服务器
                        jsonlist = sreachbyid(get_id);
                        httpJson(jsonlist.get(0),"getjson");//把最新数据传到后台
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
                menu.add(0, 1, 1, "删除");
            }

        });
        //设置收费单价
        //数据库获取收费单价
        //Config_Price.setText(intent.getStringExtra("confi_data"));
        //Config_Free_Price.setText(intent.getStringExtra("confi_free_price"));




//白天光标动态更新
        First_hour_edit.addTextChangedListener(new TextWatcher() {
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
                After_hour.setText( First_hour_edit.getText().toString());//动态更新时间
            }
        });


//白天光标动态更新
        First_min_edit.addTextChangedListener(new TextWatcher() {
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
                After_hour_min.setText(First_min_edit.getText().toString());//动态更新分钟数
            }
        });

//晚上光标动态更新
        N_First_hour_edit.addTextChangedListener(new TextWatcher() {
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
                N_After_hour.setText( N_First_hour_edit.getText().toString());//动态更新时间
            }
        });

//晚上光标动态更新
        N_First_min_edit.addTextChangedListener(new TextWatcher() {
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
                N_After_hour_min.setText(N_First_min_edit.getText().toString());//动态更新分钟数
            }
        });

        Set_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RuleConfig ruleconfig =  new RuleConfig();
                //String config_str  = Config_Price.getText().toString();
                String config_free_Price =Config_Free_Price.getText().toString();
                String first_hour_edit = First_hour_edit.getText().toString();
                String first_yuan_edit=First_yuan_edit.getText().toString();
                String first_min_edit=First_min_edit.getText().toString();
                String after_yuan_hour=After_yuan_hour.getText().toString();
                String n_free_price_edit=N_FreePrice.getText().toString();
                String n_first_hour_edit=N_First_hour_edit.getText().toString();
                String n_first_yuan_edit=N_First_yuan_hour_edit.getText().toString();
                String n_first_min_edit=N_First_min_edit.getText().toString();
                String n_after_yuan_edit=N_After_yuan_hour_edit.getText().toString();
                String day_start_date_fee=Day_start_time.getText().toString();
                String day_end_date_fee=Day_end_time.getText().toString();



                //Log.d("config_str",config_str+"======");
                if(config_free_Price==null || config_free_Price.equals("")||first_hour_edit==null || first_hour_edit.equals("")||
                first_yuan_edit==null || first_yuan_edit.equals("")||first_min_edit==null || first_min_edit.equals("") ||after_yuan_hour==null
                        ||after_yuan_hour.equals("")||n_free_price_edit==null||n_free_price_edit.equals("")||n_first_hour_edit==null||n_first_hour_edit.equals("")
                        ||n_first_yuan_edit==null||n_first_yuan_edit.equals("")||n_first_min_edit==null||n_first_min_edit.equals("")||n_after_yuan_edit==null||n_after_yuan_edit.equals("")){
                    Toast.makeText(MainActivity.this, "请输入数值", Toast.LENGTH_SHORT).show();
                }else {
                    ruleconfig.setFreePrice(Double.parseDouble(config_free_Price));
                    ruleconfig.setFirst_hour(Double.parseDouble(first_hour_edit));
                    ruleconfig.setFirst_yuan_hour(Double.parseDouble(first_yuan_edit));
                    ruleconfig.setFirst_min(Double.parseDouble(first_min_edit));
                    ruleconfig.setAfter_yuan_hour(Double.parseDouble(after_yuan_hour));
                    ruleconfig.setN_FreePrice(Double.parseDouble(n_free_price_edit));
                    ruleconfig.setN_First_yuan_hour(Double.parseDouble(n_first_hour_edit));
                    ruleconfig.setN_First_hour(Double.parseDouble(n_first_yuan_edit));
                    ruleconfig.setN_First_min(Double.parseDouble(n_first_min_edit));
                    ruleconfig.setN_After_yuan_hour(Double.parseDouble(n_after_yuan_edit));
                    ruleconfig.setDay_start_time(day_start_date_fee);
                    ruleconfig.setDay_end_time(day_end_date_fee);

                }

                B_isset_Right_time B_isset_Right_time =new B_isset_Right_time();
                try {
                    Boolean is_right_set = B_isset_Right_time.is_right_set(day_start_date_fee,day_end_date_fee);
                    if(is_right_set){
                        ruleconfig.updateAll();
                        Intent Main_intent = new Intent(MainActivity.this, MainActivity.class);
                        startActivity(Main_intent);
                        //fresh();
                        Day_start_time.setTextColor(Color.BLACK);
                        Day_end_time.setTextColor(Color.BLACK);
                        Toast.makeText(MainActivity.this, "修改成功跳转主页", Toast.LENGTH_SHORT).show();
                    }else{

                        Toast.makeText(MainActivity.this, "开始时间必须小于结束时间，修改失败", Toast.LENGTH_LONG).show();
                        Day_start_time.setTextColor(Color.RED);
                        Day_end_time.setTextColor(Color.RED);

                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                ruleconfig.updateAll();//跟新新数据库
               // b_config_all=true;//修改设置页面
                //Intent Main_intent = new Intent(MainActivity.this, MainActivity.class);
                //把设置好的参数带到主页
               // Main_intent.putExtra("confi_data", ruleconfig.getUnitPrice() + "");
                //Main_intent.putExtra("confi_free_price", ruleconfig.getFreePrice() + "");
               // startActivity(Main_intent);//跳到主页
            }
        });

//搜索页配置
        //搜索开始时间
        Search_start_date.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                        /*      Your code   to get date and time    */
                        selectedmonth = selectedmonth + 1;
                        Search_start_date.setText(String.format("%04d", selectedyear)+"-"+String.format("%02d", selectedmonth)+"-"+ String.format("%02d", selectedday)+"" );
                    }
                }, Year,Month, Day).show();
            }
        });

        //搜索结束时间
        Search_end_date.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                new DatePickerDialog(MainActivity.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                        /*      Your code   to get date and time    */
                        selectedmonth = selectedmonth + 1;
                        Search_end_date.setText(String.format("%04d", selectedyear)+"-"+String.format("%02d", selectedmonth)+"-"+ String.format("%02d", selectedday) +"" );
                    }
                }, Year,Month, Day).show();
            }
        });

        //是否逃票查询
        Search_Steal.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radbtn = (RadioButton) findViewById(checkedId);
                String sc =radbtn.getText()+"";
                Log.d("sc===",sc);
                if(sc.equals("是")){
                    Steal_Searched="1";
                }
                if(sc.equals("否")){
                    Steal_Searched="0";
                }
                if(sc.equals("所有")){
                    Steal_Searched="";
                }
            }
        });

        Get_Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //List_Steal.clear();
                try {
                    if(Steal_Searched==null){
                        Steal_Searched="";
                    }
                    Sel_Steal(Search_start_date.getText()+"",Search_end_date.getText()+"",Steal_Searched);
                } catch (ParseException e) {
                    e.printStackTrace();
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
                    selfresh("");
                    Toast.makeText(MainActivity.this, "删除成功", Toast.LENGTH_LONG).show();
                    break;
//                case 2:
//                    ContentValues values = new ContentValues();
//                    values.put("Is_Steal", "1");//表示逃票
//                    DataSupport.update(ToDay_Trans.class, values, today.getId());
//                    list.clear();
//                    selfresh("");
//                    Toast.makeText(MainActivity.this, "逃票设置成功", Toast.LENGTH_LONG).show();
//                    break;
            }
        } catch (Exception e) {
        }
        return super.onContextItemSelected(menu);

    }

    public void getConfigSet(){
        list_ruleconfig = DataSupport.findAll(RuleConfig.class);
        for (RuleConfig rc : list_ruleconfig) {
            Config_Free_Price.setText(rc.getFreePrice()+"");
            First_hour_edit.setText(rc.getFirst_hour()+"");
            First_yuan_edit.setText(rc.getFirst_yuan_hour()+"");
            First_min_edit.setText(rc.getFirst_min()+"");
            After_yuan_hour.setText(rc.getAfter_yuan_hour()+"");
            After_hour.setText( First_hour_edit.getText().toString());
            After_hour_min.setText(First_min_edit.getText().toString());
            N_FreePrice.setText(rc.getN_FreePrice()+"");
            N_First_hour_edit.setText(rc.getN_First_hour()+"");
            N_First_yuan_hour_edit.setText(rc.getN_First_yuan_hour()+"");
            N_First_min_edit.setText(rc.getN_First_min()+"");
            N_After_yuan_hour_edit.setText(rc.getN_After_yuan_hour()+"");
            N_After_hour.setText(N_First_hour_edit.getText().toString());
            N_After_hour_min.setText(N_First_min_edit.getText().toString());
            Day_start_time.setText(rc.getDay_start_time());
            Day_end_time.setText(rc.getDay_end_time());
        }
    }


//    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
//           // webView.goBack();
//            //return true;
//        }
//        return false;
//    }

    //写文件
    public void save(){
        FileOutputStream out = null;
        BufferedWriter writer = null;
        //生成文件，写入本地

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

    private  void httpJson(Object object ,String posthttp) {
        Gson gs = new Gson();
        final String objectStr = gs.toJson(object);
        final String postinhttp=posthttp;
        Log.d("objectStr=1=1=12=2",objectStr);
        new Thread(new Runnable() {
            HttpUtil HttpUtil = new HttpUtil();
            @Override
            public void run() {
                HttpUtil.SendJson("http://myss123.tk:8088/ysf/"+postinhttp,objectStr);
            }
        }).start();

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

//插屏广告
    private BannerView getBanner() {
        String posId = getPosID();
        if( this.bv != null && this.posId.equals(posId)) {
            return this.bv;
        }
        if(this.bv != null){
            bannerContainer.removeView(bv);
            bv.destroy();
        }
        this.posId = posId;
        this.bv = new BannerView(this, ADSize.BANNER, Constants.APPID,posId);
        // 注意：如果开发者的banner不是始终展示在屏幕中的话，请关闭自动刷新，否则将导致曝光率过低。
        // 并且应该自行处理：当banner广告区域出现在屏幕后，再手动loadAD。
        bv.setRefresh(5);

        bv.setADListener(new AbstractBannerADListener() {

            @Override
            public void onNoAD(AdError error) {
                Log.i(
                        "AD_DEMO",
                        String.format("Banner onNoAD，eCode = %d, eMsg = %s", error.getErrorCode(),
                                error.getErrorMsg()));
            }

            @Override
            public void onADReceiv() {
                Log.i("AD_DEMO", "ONBannerReceive");
            }
            public void onADCloseOverlay(){
                Log.i("onADCloseOverlay", "onADCloseOverlay");
            }
        });
        bannerContainer.addView(bv);

        return this.bv;
    }


    private String getPosID() {
        return Constants.BannerPosID ;
    }
}

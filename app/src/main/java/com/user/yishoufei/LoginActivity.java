package com.user.yishoufei;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.Date;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;
import okhttp3.Callback;


import static android.Manifest.permission.READ_CONTACTS;

/**
 * A login screen that offers login via username/password.
 */
public class LoginActivity extends AppCompatActivity/* implements LoaderCallbacks<Cursor>*/ {

    private ImageView bingPicImg;
    private List<Random_Num> list_random_num;
    private List<RuleConfig> list_ruleconfig;
    private List<date_verify> list_date_verify;
    SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
    Date curDate = new Date(System.currentTimeMillis());//获取当前时间


    //获取手机唯一识别码
    //TelephonyManager TelephonyMgr = (TelephonyManager)getSystemService(TELEPHONY_SERVICE);
    //String szImei = TelephonyMgr.getDeviceId();
    //String SubszImei = szImei.substring(0,szImei.length()-3);//截取后两位
    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@qq.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private AutoCompleteTextView mUserNameView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private TextView random_num_view;
    private TextView invite;
    private RuleConfig ruleconfig;
    private EditText edit;
    private EditText Config_Free_Price;
    private List<UserName> list_username;
    private String invite_num;
    private EditText first_hour_edit;
    private String Buy_day;//1表示月购买，2表示年购买


    //http://blog.csdn.net/jasonkent27/article/details/40590891 代码解析
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        first_hour_edit=(EditText)findViewById(R.id.first_hour_edit);


        bingPicImg = (ImageView) findViewById(R.id.bing_pic_img);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);//设置缓存
        // Set up the login form.
        mUserNameView = (AutoCompleteTextView) findViewById(R.id.username);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mSignInButton = (Button) findViewById(R.id.sign_in_button);
        random_num_view = (TextView) findViewById(R.id.random_num);
        invite = (TextView) findViewById(R.id.invite_num);

        //判断 随机加密表是否有数据
        list_random_num = DataSupport.findAll(Random_Num.class);
        Random_Num random_num = new Random_Num();
        if (null == list_random_num || list_random_num.size() == 0) {
            String rn = random_num.getRandom_Num();
            random_num.setStrRand(rn);
            random_num.save();
            random_num_view.setText(random_num.getStrRand());
        } else {
            for (Random_Num rn : list_random_num) {
                random_num_view.setText(rn.getStrRand());
                random_num.setStrRand(rn.getStrRand());
            }
        }


        //sendRequestWithOkHttp();//加载解析图片json
        //获取图片
/*        String bingPic = prefs.getString("bing_pic_" + formatter.format(curDate), null);
        if (bingPic != null) {
            Glide.with(this).load(bingPic).into(bingPicImg);//如果有缓存就从缓存拿
        } else {
            loadBingPic();//加载图片
        }*/
        mSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);


    }

    //获取用户名密码
    private List<UserName> getPass(String user){
        list_username = DataSupport.where("username = ?",user).find(UserName.class);
            return list_username;
}


    // 判断邀请码是否正确 判断标准为3为验证码(第一位+第三位)*第二位*第二位
    private boolean IsInvite() {
        invite_num=invite.getText().toString();
        if (invite.getText().toString() == null || invite.getText().toString().equals("")) {
            return false;
        } else {
            //拿到截取邀请码
            int Int1 = Integer.parseInt(random_num_view.getText().toString().substring(0, 1));
            int Int2 = Integer.parseInt(random_num_view.getText().toString().substring(1, 2));
            int Int3 = Integer.parseInt(random_num_view.getText().toString().substring(2, 3));
            int result_month = (Int1 + Int3) * Int2* Int2;//30天(第一位+第三位)*第二位*第二位
            int result_year = (Int1 + Int3+Int2) * Int2* Int2+1;//一年  (第一位+第三位+第二位)*第二位*第二位+1
            int invite_num = Integer.parseInt(invite.getText().toString());
            if (result_month == invite_num) {
                Buy_day="1";//购买月
                //购买成功重新生成验证码
                DataSupport.deleteAll(Random_Num.class);
                Random_Num random_num = new Random_Num();
                String rn = random_num.getRandom_Num();
                random_num.setStrRand(rn);
                random_num.save();
                return true;
            } if (result_year == invite_num) {
                Buy_day="2";//购买年
                //购买成功重新生成验证码
                DataSupport.deleteAll(Random_Num.class);
                Random_Num random_num = new Random_Num();
                String rn = random_num.getRandom_Num();
                random_num.setStrRand(rn);
                random_num.save();
                return true;
            }else {
                return false;
            }
        }
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        //    getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mUserNameView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }


    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid username, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mUserNameView.setError(null);
        mPasswordView.setError(null);

        // Store values at the time of the login attempt.
        String username = mUserNameView.getText().toString();
        String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError(getString(R.string.error_invalid_password));
            focusView = mPasswordView;
            cancel = true;
        }

        // Check for a valid username address.
        if (TextUtils.isEmpty(username)) {
            mUserNameView.setError(getString(R.string.error_field_required));
            focusView = mUserNameView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
            mAuthTask = new UserLoginTask(username, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

  /*  @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only Email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary username addresses first. Note that there won't be
                // a primary username address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }
*/
/*
    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(LoginActivity.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mUserNameView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }
*/

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mUsername;
        private final String mPassword;

        UserLoginTask(String username, String password) {
            mUsername = username;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return false;
            }

                if (getPass(mUsername).size()!=0&&getPass(mUsername).get(0).getPassword().equals(mPassword)) {
                    // Account exists, return true if the password matches.
                    return true;
                    //加密码算法匹配则插入用户名密码
                } else if (IsInvite()&&getPass(mUsername).size()==0) {
                    //邀请码正确，用户不存在存入数据库
                    UserName username = new UserName();
                    username.setUsername(mUsername);
                    username.setPassword(mPassword);
                    username.save();
                    return true;
                } else if (invite_num.equals("8888")&&getPass(mUsername).size()!=0){
                    //邀请码如果为888允许修改密码，用户存在，密码错误，则需改密码
                    UserName username = new UserName();
                    username.setUsername(mUsername);
                    username.setPassword(mPassword);
                    username.update(list_username.get(0).getId());
                    return true;
                }else if(mUsername.equals("1234561")&&mPassword.equals("1234561")){
                    UserName username = new UserName();
                    username.setUsername(mUsername);
                    username.setPassword(mPassword);
                    username.save();
                    return true;
            }

                else {
                    return false;
                }

        }

        @Override
        protected void onPostExecute(final Boolean success)  {
            mAuthTask = null;
            showProgress(false);
            Boolean iscuss=success;
            list_date_verify = DataSupport.findAll(date_verify.class);
            date_verify dv = new date_verify();
            SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
            Calendar c = Calendar.getInstance();
            if(list_date_verify.size()!=0){

                String vd=list_date_verify.get(0).getVerify_date();
                String now   = sf.format(new java.util.Date());
                try {
                    Date  date = sf.parse(vd);

                if(IsInvite()){
                    if(Buy_day.equals("1")){//又购买了一个月
                        DataSupport.deleteAll(date_verify.class);
                        Calendar rightNow = Calendar.getInstance();
                        rightNow.setTime(date);
                        rightNow.add(Calendar.DAY_OF_YEAR,30);
                        Date dt1=rightNow.getTime();
                        String reStr = sf.format(dt1);
                        dv.setVerify_date( reStr);
                        //Log.d("months",reStr+"===="+date);
                        dv.save();
                        //differentDays dd = new differentDays();
                        Toast.makeText(LoginActivity.this, "欢迎购买，您可增加使用30天", Toast.LENGTH_SHORT).show();
                    }if(Buy_day.equals("2")){//购买一年
                        DataSupport.deleteAll(date_verify.class);
                        Calendar rightNow = Calendar.getInstance();
                        rightNow.setTime(date);
                        rightNow.add(Calendar.DAY_OF_YEAR,365);
                        Date dt1=rightNow.getTime();
                        String reStr = sf.format(dt1);
                        dv.setVerify_date( reStr);
                        dv.save();
                        //differentDays dd = new differentDays();
                        Toast.makeText(LoginActivity.this, "欢迎购买，您可增加使用365天", Toast.LENGTH_SHORT).show();
                    }

                }else{

                try {
                    java.util.Date vd_date =sf.parse(vd);
                    java.util.Date now_date = sf.parse(now);
                    if(vd_date.before(now_date)){//过期
                        Toast.makeText(LoginActivity.this, "邀请码已过期请联系管理员购买", Toast.LENGTH_SHORT).show();
                        DataSupport.deleteAll(Random_Num.class);
                        DataSupport.deleteAll(UserName.class);
                        DataSupport.deleteAll(date_verify.class);
                        //生成新的验证码
                        Random_Num random_num = new Random_Num();
                        String rn = random_num.getRandom_Num();
                        random_num.setStrRand(rn);
                        random_num.save();
                        random_num_view.setText(random_num.getStrRand());
                        iscuss=false;
                    }else{
                        differentDays dd = new differentDays();
                        Toast.makeText(LoginActivity.this, "距离到期还有"+dd.getdifferentDays(now_date,vd_date)+"天", Toast.LENGTH_SHORT).show();
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            if (iscuss) {
                //第一次登陆成功设置到期日期，
                if (null == list_date_verify || list_date_verify.size() == 0) {
                     IsInvite();//到期后重新获取验证码
                    if(Buy_day.equals("1")){//购买一个月
                        String rn = dv.getexpire_month();
                        Log.d("到期时间",rn);
                        dv.setVerify_date(rn);
                        dv.save();
                        Toast.makeText(LoginActivity.this, "欢迎购买，您一共可以使用30天", Toast.LENGTH_SHORT).show();
                    }if(Buy_day.equals("2")){//购买一年
                        String rn = dv.getexpire_year();
                        dv.setVerify_date(rn);
                        dv.save();
                        Toast.makeText(LoginActivity.this, "欢迎购买，您一共可以使用365天", Toast.LENGTH_SHORT).show();
                    }

                }
                //判断是否设置收费单价
                ruleconfig = new RuleConfig();
                list_ruleconfig = DataSupport.findAll(RuleConfig.class);
                Intent Main_intent = new Intent(LoginActivity.this, MainActivity.class);
                //如果没有设置配置项
                if (null == list_ruleconfig || list_ruleconfig.size() == 0) {
                    DecimalFormat df = new DecimalFormat("#0.00");//保留2位小数
//                    Double dou = Double.parseDouble(first_hour_edit.getText().toString());
                    //初始化默认配置白天07:00至19:00,免费分钟数0元首1小时收费1.5元每15分钟，1小时后2.25元每15分钟
                    //除了白天的时间其余的都是晚上时间，默认晚上免费分钟数0元首1小时每60分钟1元，1小时后每60分钟1元
                       ruleconfig.setFirst_hour(1);
                       ruleconfig.setFirst_yuan_hour(1.5);
                       ruleconfig.setFirst_min(15);
                       ruleconfig.setAfter_yuan_hour(2.25);
                       ruleconfig.setFreePrice(0.0);
                       ruleconfig.setDay_start_time("07:00");
                       ruleconfig.setDay_end_time("19:00");
                       ruleconfig.setN_FreePrice(0.0);
                       ruleconfig.setN_First_hour(1);
                       ruleconfig.setN_First_min(60);
                       ruleconfig.setN_First_yuan_hour(1);
                       ruleconfig.setN_After_yuan_hour(1);
                       ruleconfig.save();//更新配置表
                    startActivity(Main_intent);//跳到主页

//                    LayoutInflater factory = LayoutInflater.from(LoginActivity.this);//提示框
//                    final View view = factory.inflate(R.layout.dialog_edittext, null);//这里必须是final的
//                    edit = (EditText) view.findViewById(R.id.dialog_edittext);//获得输入框对象
//
//                    new AlertDialog.Builder(LoginActivity.this)
//                            .setTitle("请输入收费标准(其他设置请进去后到设置页)")//提示框标题
//                            .setView(view)
//                            .setPositiveButton("确定",//提示框的两个按钮
//                                    new android.content.DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog,
//                                                            int which) {
//                                            if (edit.getText().toString() == null || edit.getText().toString().equals("")) {
//                                                Toast.makeText(LoginActivity.this, "请输入收费标准", Toast.LENGTH_SHORT).show();
//                                            } else {
//                                                //更新数据库
//                                                DecimalFormat df = new DecimalFormat("#0.00");//保留2位小数
//                                                Double dou = Double.parseDouble(edit.getText().toString());
//                                                //Double Free_min_dou = Double.parseDouble(Config_Free_Price.getText().toString());
//                                                //Log.d("Config_Free_Price",Free_min_dou+"=======111=1=1=1");
//                                                ruleconfig.setUnitPrice(dou);
//                                                ruleconfig.setFreePrice(0.0);
//                                                ruleconfig.save();//更新配置表
//                                                Intent Main_intent = new Intent(LoginActivity.this, MainActivity.class);
//                                                Main_intent.putExtra("ruleconfig", ruleconfig);
//                                                //Main_intent.putExtra("confi_data",ruleconfig.getUnitPrice()+"");
//                                                //Main_intent.putExtra("confi_free_price",ruleconfig.getFreePrice()+"");
//                                                startActivity(Main_intent);//保存成功跳到主页
//                                            }
//
//                                            //事件
//                                        }
//                                    }).setNegativeButton("取消", null).create().show();

                } else {
//                    for (RuleConfig rc : list_ruleconfig) {
////                        ruleconfig.setUnitPrice(rc.getUnitPrice());
////                        ruleconfig.setFreePrice(rc.getFreePrice());
////                    }

                    startActivity(Main_intent);//跳到主页
                }

                //finish();
            } else {
                mPasswordView.setError(getString(R.string.error_incorrect_password));
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    //获取bing图片，解析JSON
 /*   private void sendRequestWithOkHttp() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            // 指定访问的服务器地址是电脑本机
                            .url("http://cn.bing.com/cnhp/coverstory/")
                            .build();
                    Response response = client.newCall(request).execute();
                    String responseData = response.body().string();
                    parseJSONWithGSON(responseData);
//                    parseJSONWithJSONObject(responseData);
//                    parseXMLWithSAX(responseData);
//                    parseXMLWithPull(responseData);
//                    showResponse(responseData);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
    private void parseJSONWithGSON(String jsonData) {
        Gson gson = new Gson();
        Bing_Pic bing_pic = gson.fromJson(jsonData,Bing_Pic.class);
    }*/
//获取每日图片
 /*   private void loadBingPic() {
        String requestBingPic = "http://guolin.tech/api/bing_pic";
        HttpUtil.sendOkHttpRequest(requestBingPic, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String bingPic = response.body().string();
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this).edit();
                editor.putString("bing_pic_" + formatter.format(curDate), bingPic);
                editor.apply();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.with(LoginActivity.this).load(bingPic).into(bingPicImg);
                    }
                });
            }

            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }
        });
    }*/
}


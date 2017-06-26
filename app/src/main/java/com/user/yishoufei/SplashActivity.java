package com.user.yishoufei;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.qq.e.ads.splash.SplashAD;
import com.qq.e.ads.splash.SplashADListener;

import java.util.ArrayList;
import java.util.List;

public class SplashActivity extends AppCompatActivity {

    private RelativeLayout container;
    /*用来判断是否可以跳过广告*/
    private boolean canJump;
    private TextView skipView;
    private static final String SKIP_TEXT = "点击跳过 %d";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        container =(RelativeLayout) findViewById(R.id.container);
        skipView = (TextView) findViewById(R.id.skip_view);
        //进行权限处理
        List<String> permissionList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!permissionList.isEmpty()){
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(this,permissions,1);
        }else {
            requestAds();
        }

    }
    private void requestAds(){
    String appId ="1106242054";
    String adId="9030125336703188";
        //String appId ="1101152570";
        //String adId="8863364436303842593";
    new SplashAD(this,container,appId,adId,new SplashADListener(){
        @Override
         public void onADDismissed(){
            //广告显示完毕
            forward();
        }
        @Override
        public void onNoAD(int i){
            //广告加载失败
            forward();
        }
        @Override
        public void onADPresent(){
            //广告加载成功
        }
        @Override
        public void onADClicked(){
            //广告被点击
        }
        @Override
        public void onADTick(long millisUntilFinished) {
            Log.i("AD_DEMO", "SplashADTick " + millisUntilFinished + "ms");
            skipView.setText(String.format(SKIP_TEXT, Math.round(millisUntilFinished / 1000f)));
        }

    });
    }
    @Override
    protected  void onPause(){
        super.onPause();
        canJump=false;
    }
    @Override
    protected void onResume(){
        super.onResume();
        if (canJump){
            forward();
        }
        canJump=true;
    }

    private  void forward(){
        if (canJump){
            //跳转到LoginActivity
            Intent intent = new Intent(this,LoginActivity.class);
            startActivity(intent);
            finish();
        }else {
            canJump=true;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions,
                                           int[] grantResults){
        switch (requestCode){
            case 1:
                if(grantResults.length>0){
                    for(int result : grantResults){
                        if(result!=PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this,"为了更好的使用请同意权限",Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                    requestAds();
                }else {
                    Toast.makeText(this,"发生未知错误",Toast.LENGTH_SHORT).show();
                    finish();

                }
                break;
            default:
        }
    }
}

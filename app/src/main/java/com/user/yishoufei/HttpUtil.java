package com.user.yishoufei;

import android.util.Log;


import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public  class HttpUtil {
    private static OkHttpClient client = null;

//    public static void sendOkHttpRequest(String address) {
//        OkHttpClient client = new OkHttpClient();
//        RequestBody body = new FormBody.Builder()
//                .add("username", "中")//添加键值对
//                .add("password", "123").build();
//        Request request = new Request.Builder().url(address).addHeader("Accept-Charset","utf-8").post(body).build();
//        try {
//        Response response = client.newCall(request).execute();
//        if(response.isSuccessful())
//        {
//            String str = response.body().string();
//            System.out.println("服务器响应为: " + str);
//        }
//        else{
//            System.out.print("shibai");
//        }
//        }catch(IOException ex) {
//            //Do something with the exception
//        }
//
//    }

    public   void SendJson(String url, String jsonParams) {
        OkHttpClient client = new OkHttpClient();
       // Log.d("jsonParams==11=1=1",jsonParams);
        RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8")
                , jsonParams);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if(response.isSuccessful())
            {
                String str = response.body().string();
                System.out.println("服务器响应为: " + str);
            }
            else{
                System.out.print("shibai");
            }
        }catch(IOException ex) {
            //Do something with the exception
        }

    }
    public  OkHttpClient getInstance() {
        if (client == null) {
            synchronized (HttpUtil.class) {
                if (client == null)
                    client = new OkHttpClient();
            }
        }
        return client;
    }


//    public static void main(String[] args) throws IOException{
//       // sendOkHttpRequest("http://localhost:8080/ysf/logon");
//            //UserName  UserName = new UserName();
//            //UserName.setUsername("我的");
//           // UserName.setPassword("aaaa");
//                String jsonStr = "{\"name\":\"1\",\"age\":1,\"id\":0}";
//            //JSONObject json = JSONObject.fromObject(UserName);
//            //String strJson=json.toString();
//            //System.out.print("=1=1=1=1"+strJson);
//            SendJson("http://localhost:8080/ysf/getjson",jsonStr);
//    }


    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(JSON, json);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            return response.body().string();
        } else {
            throw new IOException("Unexpected code " + response);
        }
    }



    }


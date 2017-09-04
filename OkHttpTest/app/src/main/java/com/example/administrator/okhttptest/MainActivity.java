package com.example.administrator.okhttptest;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.util.OkManager;
import com.example.util.VG;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {

    private Button testButton, getJsonButton, button3;
    private ImageView testImageView;
    private final static int SUCCESS_SATUS = 1;
    private final static int FAILURE = 0;
    private final static String Tag = MainActivity.class.getSimpleName();
    private Handler handler;

    private OkManager manager;

    private OkHttpClient clients;

    //图片下载的请求地址
    private String img_path = "http://10.0.0.2:8080/OkHttp3Server/UploadDownloadServlet?method=download";
    //请求返回值为Json数组
    private String jsonpath = "http://www.baidu.com";//"http://10.0.0.2:8080/OkHttp3Server/ServletJson";
    //登录验证请求
    private String login_path="http://192.168.11.96:8080/myhappyform/jlLoginAction_phoneLogin";
    private String result;
    private JSONObject reJsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        handler=new Handler(){
//            @Override
//            public void handleMessage(Message msg) {
//                Log.i("info","现在已经到han"+result);
//                Intent intent=new Intent(MainActivity.this,SecondActivity.class);
//                Gson gson=new Gson();
//                Map map=gson.fromJson(result,Map.class);
//                if((Boolean)map.get("msg")){
//                    Bundle b=new Bundle();
//                    b.putString("username",(String)map.get("username"));
//                    intent.putExtras(b);
//                    startActivity(intent);
//                    MainActivity.this.finish();
//                }
//
//                super.handleMessage(msg);
//
//            }
//        };
        testButton = (Button) findViewById(R.id.test);
        getJsonButton = (Button) findViewById(R.id.getjson);


        testImageView = (ImageView) findViewById(R.id.testImageView);
        button3 = (Button) findViewById(R.id.button3);

        //-----------------------------------------------------------------------------------------
        manager = OkManager.getInstance();
        getJsonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.asyncStringByURL("http://www.baidu.com",new OkManager.returnString(){
                    public void onResponse(String html) {
                        Log.i("get info",html);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                getJsonButton.setText("已经改变了哦");
                            }
                        });

                    }
                });
            }
        });
        //-------------------------------------------------------------------------
        //用于登录请求测试，登录用户名和登录密码应该Sewrver上的对应
        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map map=new HashMap();
                map.put("username","admin");
                map.put("password","666666");

                manager.sendComplexForm(login_path, map, new OkManager.returnJson() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        Log.i(Tag, jsonObject.toString());
                        reJsonObject=jsonObject;
                        runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            button3.setText("已经改变了哦");
                            boolean flag;
                            Bundle b=new Bundle();
                            try {
                                flag=(Boolean)reJsonObject.get("msg");
                                VG.USERINFO=(JSONObject)reJsonObject.get("data");
                                b.putString("username",(String)VG.USERINFO.get("username"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Intent intent=new Intent(MainActivity.this,SecondActivity.class);



                            intent.putExtras(b);
                            startActivity(intent);
                            MainActivity.this.finish();
                        }
                    });
                    }
                });
            }
        });
        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                manager.asyncDownLoadImgtByUrl(img_path, new OkManager.returnBitMap() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        // testImageView.setBackgroundResource(0);
                        testImageView.setImageBitmap(bitmap);
                        Log.i(Tag, "231541645");
                    }
                });
            }
        });

    }



}

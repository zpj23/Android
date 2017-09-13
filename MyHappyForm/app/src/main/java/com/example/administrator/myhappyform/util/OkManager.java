package com.example.administrator.myhappyform.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/8/22.
 * OkHttpClient工具类
 */

public class OkManager {
    private OkHttpClient client;
    private volatile static OkManager manager;   //防止多个线程访问时
    private final String TAG = OkManager.class.getSimpleName();  //获得类名
    private Handler handler;

    //提交json数据
    private static final MediaType JSON = MediaType.parse("application/json;charset=utf-8");
    //提交字符串数据
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/x-markdown;charset=utf-8");

    private OkManager() {
//        client = new OkHttpClient();
        client=new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .build();

        handler = new Handler(Looper.getMainLooper());
    }

    //采用单例模式获取对象
    public static OkManager getInstance() {
        OkManager instance = null;
        if (manager == null) {
            synchronized (OkManager.class) {                //同步代码块
                if (instance == null) {
                    instance = new OkManager();
                    manager = instance;
                }
            }
        }else{
            instance=manager;
        }
        return instance;
    }

    /**
     * 请求返回的是JSON字符串
     *
     * @param jsonValue
     * @param callBack
     */
    private void onSuccessJsonStringMethod(final String jsonValue, final returnString callBack) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    try {
                        callBack.onResponse(jsonValue);
                    } catch (Exception e) {

                    }
                }
            }
        });

    }

    /**
     * 请求返回相应结果的是Json对象
     *
     * @param jsonValue
     * @param callBack
     */
    private void onSuccessJsonObjectMethod(final String jsonValue, final returnJson callBack) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    try {
                        callBack.onResponse(new JSONObject(jsonValue));
                    } catch (JSONException e) {

                    }
                }
            }
        });
    }

    /**
     * 返回响应的对象是一个字节数组
     *
     * @param data
     * @param callBack
     */
    private void onSuccessByteMethod(final byte[] data, final returnByte callBack) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    try {
                        callBack.onResponse(data);
                    } catch (Exception e) {

                    }
                }
            }
        });
    }

    private void onSuccessImgMethod(final Bitmap bitmap, final returnBitMap callBack) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                if (callBack != null) {
                    try {
                        callBack.onResponse(bitmap);
                    } catch (Exception e) {

                    }

                }
            }
        });
    }

    /**
     * 同步请求,在Android开发中不常用，因为会阻塞UI线程
     *
     * @param url
     * @return
     */

    public String synaGetByUrl(String url) {
        //构建一个Request请求
        Request request = new Request.Builder().url(url).build();
        Response response = null;
        try {
            response = client.newCall(request).execute();  //execute用于同步请求数据
            if (response.isSuccessful()) {
                return response.body().string();
            }
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * 异步请求,请求返回Json字符串
     *
     * @param url
     * @param callback
     */
    public void asyncStringByURL(String url, final returnString callback) {
        final Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            //enqueue是调用了一个入队的方法
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onSuccessJsonStringMethod(response.body().string(), callback);
                }
            }
        });

    }

    /**
     * 异步请求，请求返回Json对象
     *
     * @param url
     * @param callback
     */

    public void asyncJsonObjectByUrl(String url, final returnJson callback) {
        final Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onSuccessJsonObjectMethod(response.body().string(), callback);
                }
            }
        });

    }

    /**
     * 异步请求，请求返回的byte字节数组
     *
     * @param url
     * @param callback
     */
    public void asyncGetByteByUrl(String url, final returnByte callback) {
        final Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onSuccessByteMethod(response.body().bytes(), callback);
                }
            }
        });
    }

    /**
     * 异步请求，请求返回图片
     *
     * @param url
     * @param callback
     */
    public void asyncDownLoadImgtByUrl(String url, final returnBitMap callback) {
        final Request request = new Request.Builder().url(url).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    byte[] data = response.body().bytes();
                    Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    onSuccessImgMethod(bitmap, callback);

                    System.out.println(data.length);
                }
            }
        });

    }

    /**
     * 模拟表单的提交
     *
     * @param url
     * @param param
     * @param callback
     */
    public void sendComplexForm(String url, Map<String, String> param, final returnJson callback) {
        FormBody.Builder form_builder = new FormBody.Builder();  //表单对象，包含以input开始的对象，模拟一个表单操作，以HTML表单为主
        //如果键值对不为空，且值不为空
        if (param != null && !param.isEmpty()) {
            //循环这个表单，zengqiang for循环
            for (Map.Entry<String, String> entry : param.entrySet()) {
                form_builder.add(entry.getKey(), entry.getValue());
            }
        }
        //声明一个请求对象体
        RequestBody request_body = form_builder.build();
        //采用post的方式进行提交
        Request request = new Request.Builder().url(url).post(request_body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("info","请求失败");
                JSONObject josb=new JSONObject();
                try {
                    josb.put("msg",false);
                } catch (JSONException e1) {
                    e1.printStackTrace();
                }
                onSuccessJsonObjectMethod(josb.toString(), callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    String str=response.body().string();
                    Log.i("info",str);
                    onSuccessJsonObjectMethod(str, callback);
                }
            }
        });
    }

    /**
     * 向服务器提交String请求
     *
     * @param url
     * @param content
     * @param callback
     */
    public void sendStringByPost(String url, String content, final returnJson callback) {
        Request request = new Request.Builder().url(url).post(RequestBody.create(MEDIA_TYPE_MARKDOWN, content)).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response != null && response.isSuccessful()) {
                    onSuccessJsonObjectMethod(response.body().string(), callback);
                }
            }
        });

    }

    //回调
    public interface returnString {
        void onResponse(String result);
    }

    interface returnByte {
        void onResponse(byte[] result);
    }

    public interface returnBitMap {
        void onResponse(Bitmap bitmap);
    }

    public interface returnJson {
        void onResponse(JSONObject jsonObject);
    }

    public  void executeGet() throws Exception {
        Request request = new Request.Builder()
                .url("http://www.baidu.com")
                .build();
        Response response = client.newCall(request).execute();
        if(response.isSuccessful()){
            System.out.println(response.code());
            Log.i("response",response.body().string());
            System.out.println(response.body().string());
        }
    }

    private void getAsynHttp() {

        Request.Builder requestBuilder = new Request.Builder().url("http://www.baidu.com");
        //可以省略，默认是GET请求
        requestBuilder.method("GET",null);
        Request request = requestBuilder.build();
        Call mcall= client.newCall(request);
        mcall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i("info","");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (null != response.cacheResponse()) {
                    String str = response.cacheResponse().toString();
                    Log.i("wangshu", "cache---" + str);
                } else {
                    String string=response.body().string();
                    String str = response.networkResponse().toString();
                    Log.i("wangshu", "network---" + string);
                    Log.i("wangshu", "network---" + str);
                }
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        button.setText("已经改变了哦");
////                        Toast.makeText(getApplicationContext(), "请求成功", Toast.LENGTH_SHORT).show();
//                    }
//                });
            }
        });
    }
}

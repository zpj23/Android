package com.example.administrator.myhappyform.service;

import android.Manifest;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.SystemClock;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.example.administrator.myhappyform.receiver.AlarmReceiver;
import com.example.administrator.myhappyform.util.OkManager;
import com.example.administrator.myhappyform.util.VG;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Administrator on 2017/9/29.
 */

public class LocationService  extends IntentService {
    private String provider;//位置提供器
    private LocationManager locationManager;//位置服务
    private Location location;
    private String latitude;//纬度
    private String longitude;//经度
    OkManager manager;
    public LocationService() {
        super("定位服务");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        initLocation();
        AlarmManager manager1 = (AlarmManager) getSystemService(ALARM_SERVICE);
        //这里是定时的,这里设置的是每隔两秒打印一次时间=-=,自己改
        int anHour = 10 * 1000;
        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;
        Intent i = new Intent(this,AlarmReceiver.class);
        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);
//        manager1.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            manager1.setExactAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            manager1.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        }else{
            manager1.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(),anHour, pi);
        }
    }
    public void initLocation(){
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);//获得位置服务
        provider = judgeProvider(locationManager);

        if (provider != null) {//有位置提供器的情况
            //为了压制getLastKnownLocation方法的警告
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            location = locationManager.getLastKnownLocation(provider);
            if (location != null) {
                getLocation(location);//得到当前经纬度并开启线程去反向地理编码
//                locationManager.requestLocationUpdates(provider, 2000, 10,
//                        locationListener);// 产生位置改变事件的条件设定为距离改变10米，时间间隔为2秒，设定监听位置变化
            } else {
                //tv_show.setText("暂时无法获得当前位置");
            }
        }else{//不存在位置提供器的情况

        }
    }
    private String judgeProvider(LocationManager locationManager) {
        List<String> prodiverlist = locationManager.getProviders(true);
        if(prodiverlist.contains(LocationManager.NETWORK_PROVIDER)){
            return LocationManager.NETWORK_PROVIDER;
        }else if(prodiverlist.contains(LocationManager.GPS_PROVIDER)) {
            return LocationManager.GPS_PROVIDER;
        }else{
            // Toast.makeText(MainActivity.this,"没有可用的位置提供器",Toast.LENGTH_SHORT).show();
        }
        return null;
    }

//    private final LocationListener locationListener=new LocationListener() {
//        @Override
//        public void onLocationChanged(Location location) {
//            getLocation(location);
//        }
//
//        @Override
//        public void onStatusChanged(String provider, int status, Bundle extras) {
//            getLocation(null);
//        }
//
//        @Override
//        public void onProviderEnabled(String provider) {
//            getLocation(null);
//        }
//
//        @Override
//        public void onProviderDisabled(String provider) {
//
//        }
//    };

    public void updateLocation(String latitude,String longitude,String address){
        manager=OkManager.getInstance();
        String url= VG.UPDATE_LOCATION_PATH +"?id="+VG.USERINFO.getId()+"&latitude="+latitude+"&longitude="+longitude+"&address="+address;
        manager.asyncStringByURL(url, new OkManager.returnString() {
            @Override
            public void onResponse(String str) {

                if(str.equalsIgnoreCase("success")){
                    Log.i("111","更新成功");
                }

            }
        });
    }
    /**
     * 得到当前经纬度并开启线程去反向地理编码
     */
    public void getLocation(Location location) {
        latitude = location.getLatitude()+"";
        longitude = location.getLongitude()+"";
        // String url = "http://api.map.baidu.com/geocoder/v2/?ak=pPGNKs75nVZPloDFuppTLFO3WXebPgXg&callback=renderReverse&location="+latitude+","+longitude+"&output=json&pois=0";
        String url = "http://api.map.baidu.com/geocoder/v2/?ak=sNyur13vORywDFGWIkwSmuDi&callback=renderReverse&location="+latitude+","+longitude+"&output=json&pois=0";
        manager=OkManager.getInstance();
        manager.asyncStringByURL(url, new OkManager.returnString() {
            @Override
            public void onResponse(String str) {
                try {
                    str = str.replace("renderReverse&&renderReverse","");
                    str = str.replace("(","");
                    str = str.replace(")","");
                    JSONObject jsonObject = new JSONObject(str);
                    JSONObject address = jsonObject.getJSONObject("result");
                    String city = address.getString("formatted_address");
                    String district = address.getString("sematic_description");
                    updateLocation(latitude,longitude,city+district);
//                    tv_show.setText("当前位置："+city+district);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}

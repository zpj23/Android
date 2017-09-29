package com.example.administrator.showlocation;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    private String provider;//位置提供器
    private LocationManager locationManager;//位置服务
    private Location location;
    private Button btn_show;
    private TextView tv_show;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();//关联控件
        initLocation();
    }
    private void init() {
        btn_show = (Button) findViewById(R.id.btn_show_location);
        tv_show = (TextView) findViewById(R.id.tv_location_show);
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
                locationManager.requestLocationUpdates(provider, 2000, 10,
                        locationListener);// 产生位置改变事件的条件设定为距离改变10米，时间间隔为2秒，设定监听位置变化
            } else {
                tv_show.setText("暂时无法获得当前位置");
            }
        }else{//不存在位置提供器的情况

        }
    }
    /**
     * 得到当前经纬度并开启线程去反向地理编码
     */
    public void getLocation(Location location) {
        String latitude = location.getLatitude()+"";
        String longitude = location.getLongitude()+"";
       // String url = "http://api.map.baidu.com/geocoder/v2/?ak=pPGNKs75nVZPloDFuppTLFO3WXebPgXg&callback=renderReverse&location="+latitude+","+longitude+"&output=json&pois=0";
        String url = "http://api.map.baidu.com/geocoder/v2/?ak=sNyur13vORywDFGWIkwSmuDi&callback=renderReverse&location="+latitude+","+longitude+"&output=json&pois=0";
        OkManager manager=OkManager.getInstance();
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
                    tv_show.setText("当前位置："+city+district);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

//        new MyAsyncTask(url).execute();
    }

    /**
     * 判断是否有可用的内容提供器
     * @return 不存在返回null
     */
    private String judgeProvider(LocationManager locationManager) {
        List<String> prodiverlist = locationManager.getProviders(true);
        if(prodiverlist.contains(LocationManager.NETWORK_PROVIDER)){
            return LocationManager.NETWORK_PROVIDER;
        }else if(prodiverlist.contains(LocationManager.GPS_PROVIDER)) {
            return LocationManager.GPS_PROVIDER;
        }else{
            Toast.makeText(MainActivity.this,"没有可用的位置提供器",Toast.LENGTH_SHORT).show();
        }
        return null;
    }

//    class MyAsyncTask extends AsyncTask<Void,Void,Void> {
//        String url = null;//要请求的网址
//        String str = null;//服务器返回的数据
//        String address = null;
//        public MyAsyncTask(String url){
//            this.url = url;
//        }
//        @Override
//        protected Void doInBackground(Void... params) {
//            OkManager manager=OkManager.getInstance();
//            manager.asyncStringByURL(url, new OkManager.returnString() {
//                @Override
//                public void onResponse(String result) {
//                    str=result;
//                }
//            });
//
//
////            str = GetHttpConnectionData.getData(url);
//            return null;
//        }
//        @Override
//        protected void onPostExecute(Void aVoid) {
//            try {
//                str = str.replace("renderReverse&&renderReverse","");
//                str = str.replace("(","");
//                str = str.replace(")","");
//                JSONObject jsonObject = new JSONObject(str);
//                JSONObject address = jsonObject.getJSONObject("result");
//                String city = address.getString("formatted_address");
//                String district = address.getString("sematic_description");
//                tv_show.setText("当前位置："+city+district);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//            super.onPostExecute(aVoid);
//        }
//    }
    private final LocationListener locationListener=new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            getLocation(location);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            getLocation(null);
        }

        @Override
        public void onProviderEnabled(String provider) {
            getLocation(null);
        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
}

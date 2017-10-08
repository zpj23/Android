package com.example.administrator.myhappyform;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.widget.TextView;

import com.example.administrator.myhappyform.fragment.ContentFragment;
import com.example.administrator.myhappyform.service.LocationService;

/**
 * Created by Administrator on 2017/9/6.
 */

public class MainActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setDefaultFragment();
        //initMenu();

        startLocationService();
    }

    public void startLocationService(){
        Intent intent = new Intent(this,LocationService.class);
        startService(intent);
    }
//    public void stopLoactionService(){
//        Intent intent = new Intent();
//        intent.setAction("locationService");
//        stopService(intent);
//    }

    public   void  setTitle(String title){
        TextView textView=(TextView)findViewById(R.id.zy);
        textView.setText(title);
    }

    private void setDefaultFragment()
    {
        FragmentManager fm = getFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        ContentFragment contentFragment = new ContentFragment();
        transaction.replace(R.id.id_content, contentFragment);
        transaction.commit();
//        initMenu();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // 按下BACK，同时没有重复
            setTitle("主页");
        }
        return super.onKeyDown(keyCode, event);
    }
}

package com.example.administrator.okhttptest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import com.example.util.VG;

import org.json.JSONException;

/**
 * Created by Administrator on 2017/8/17.
 */

public class SecondActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.second_main);
        Intent intent=getIntent();

        TextView user=(TextView)findViewById(R.id.user);
        TextView pwd=(TextView)findViewById(R.id.pwd);
        user.setText("用户名："+intent.getExtras().get("username").toString());
        try {
            pwd.setText("密码："+ (String)VG.USERINFO.get("password"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}

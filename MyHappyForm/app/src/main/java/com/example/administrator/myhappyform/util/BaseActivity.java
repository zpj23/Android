package com.example.administrator.myhappyform.util;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Administrator on 2017/9/15.
 */

public class BaseActivity extends AppCompatActivity {
    public ProgressDialog pd;
    public Context currentContext;
    public void openWaiting(){
        pd = ProgressDialog.show(currentContext, "", "加载中，请稍后……");
    }
    public void closeWaiting(){
        pd.dismiss();
    }

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//    }
}

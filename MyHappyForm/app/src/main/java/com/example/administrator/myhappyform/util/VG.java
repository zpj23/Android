package com.example.administrator.myhappyform.util;

import android.app.ProgressDialog;

import com.example.administrator.myhappyform.LoginActivity;
import com.example.administrator.myhappyform.entity.UserInfo;

import org.json.JSONObject;

/**
 * Created by Administrator on 2017/9/4.
 */

public class VG {
    public static UserInfo USERINFO;
//    public static String SAVE_CHECKINFO_PATH="http://47.94.87.191/";
 //     public static String LOGIN_PATH="http://47.94.87.191/jlLoginAction_loginByPhone";
    public static String SAVE_CHECKINFO_PATH="http://192.168.11.96:8080/jlManualCheckInfoAction_saveInfoByPhone";
    public static String LOGIN_PATH="http://192.168.11.96:8080/jlLoginAction_loginByPhone";
    public static String LIST_CHECKINFO_PATH="http://192.168.11.96:8080/jlManualCheckInfoAction_findListInfoByPhone";
    public static String FIND_CHECKINFO_BYID_PATH="http://192.168.11.96:8080/jlManualCheckInfoAction_findInfoByIdByPhone";

}

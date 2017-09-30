package com.example.administrator.myhappyform.util;

import android.app.ProgressDialog;

import com.example.administrator.myhappyform.LoginActivity;
import com.example.administrator.myhappyform.entity.UserInfo;

import org.json.JSONObject;

import java.io.FileInputStream;

/**
 * Created by Administrator on 2017/9/4.
 */

public class VG {
    public static UserInfo USERINFO;
    public static Boolean isExit=false;
    public static String BASEPATH="http://47.94.87.191/";
//    public static String BASEPATH="http://192.198.11.96:8080/";
    public static String SAVE_CHECKINFO_PATH=BASEPATH +"jlManualCheckInfoAction_saveInfoByPhone";
    public static String LOGIN_PATH=BASEPATH +"jlLoginAction_loginByPhone";
    public static String LIST_CHECKINFO_PATH=BASEPATH +"jlManualCheckInfoAction_findListInfoByPhone";
    public static String FIND_CHECKINFO_BYID_PATH=BASEPATH +"jlManualCheckInfoAction_findInfoByIdByPhone";
    public static String UPDATE_LOCATION_PATH=BASEPATH +"jlLocationAction_updateLocationByPhone";

}

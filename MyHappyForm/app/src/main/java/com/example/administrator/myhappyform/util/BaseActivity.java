package com.example.administrator.myhappyform.util;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.administrator.myhappyform.entity.UserInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/15.
 */

public class BaseActivity extends AppCompatActivity {
    public ProgressDialog pd;
    public Context currentContext;
    public Gson gson=new Gson();
    public Map requestMap;
    public AlertDialog ad;
    public AlertDialog.Builder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(VG.isExit){
            delLoginFile();
            VG.USERINFO=null;
            VG.isExit=false;
        }
        getRequestMap();
//        requestMap=new HashMap();
//        if(VG.USERINFO!=null) {
//            requestMap.put("loginId", VG.USERINFO.getId());
//            requestMap.put("isAdmin", VG.USERINFO.getIsAdmin());
//        }else{
//            String str=readInFile();
//            if(!str.equalsIgnoreCase("")) {
//                Type listType = new TypeToken<UserInfo>() {}.getType();
//                UserInfo userInfo=gson.fromJson(str, listType);
//                VG.USERINFO = gson.fromJson(str, listType);
//                requestMap.put("loginId", VG.USERINFO.getId());
//                requestMap.put("isAdmin", VG.USERINFO.getIsAdmin());
//            }
//        }
    }

    public void openWaiting(){
        if(pd!=null&&pd.isShowing()){
            pd.dismiss();
        }
        pd = ProgressDialog.show(currentContext, "", "加载中，请稍后……");
    }
    public void closeWaiting(){
        if(pd!=null&&pd.isShowing()){
            pd.dismiss();
        }
    }

//    public void showSaveAlert(){
//        if(builder==null){
//            builder = new AlertDialog.Builder(currentContext);
//        }
//        if(ad!=null&&ad.isShowing()){
//            ad.hide();
//        }
//        ad = builder
//                .setTitle("系统提示：")
//                .setMessage("保存成功")
//
//                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        ad.hide();
//                    }
//                })
//               .create();             //创建AlertDialog对象
//        ad.show();
//
//    }




//    核心原理: Context提供了两个方法来打开数据文件里的文件IO流 FileInputStream openFileInput(String name); FileOutputStream(String name , int mode),这两个方法第一个参数 用于指定文件名，第二个参数指定打开文件的模式。具体有以下值可选：
//
//    MODE_PRIVATE：为默认操作模式，代表该文件是私有数据，只能被应用本身访问，在该模式下，写入的内容会覆盖原文件的内容，如果想把新写入的内容追加到原文件中。可   以使用Context.MODE_APPEND
//
//    MODE_APPEND：模式会检查文件是否存在，存在就往文件追加内容，否则就创建新文件。
//
//    MODE_WORLD_READABLE：表示当前文件可以被其他应用读取；
//
//    MODE_WORLD_WRITEABLE：表示当前文件可以被其他应用写入。
//
//    除此之外，Context还提供了如下几个重要的方法：
//
//    getDir(String name , int mode):在应用程序的数据文件夹下获取或者创建name对应的子目录
//
//    File getFilesDir():获取该应用程序的数据文件夹得绝对路径
//
//    String[] fileList():返回该应用数据文件夹的全部文件

    public String readInFile() {
        try {
            File file=getFilesDir();
            boolean isExist=isExist(file.getPath()+"/hp.txt");
            StringBuilder sb = new StringBuilder("");
            if(isExist){
                FileInputStream inStream = this.openFileInput("hp.txt");
                byte[] buffer = new byte[1024];
                int hasRead = 0;

                while ((hasRead = inStream.read(buffer)) != -1) {
                    sb.append(new String(buffer, 0, hasRead));
                }

                inStream.close();
            }

            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public void writeInFile(String msg){
        // 步骤1：获取输入值
        if(msg == null) return;
        try {
            // 步骤2:创建一个FileOutputStream对象,MODE_APPEND追加模式
            FileOutputStream fos = openFileOutput("hp.txt",
                    MODE_PRIVATE);
            // 步骤3：将获取过来的值放入文件
            fos.write(msg.getBytes());
            // 步骤4：关闭数据流
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public  boolean isExist(String path) {
        File file = new File(path);
//判断文件夹是否存在,如果不存在则创建文件夹
        if (!file.exists()) {
           return false;
        }else{
            return  true;
        }
    }
    public  void delLoginFile() {
        File file=getFilesDir();
        File f = new File(file.getPath()+"/hp.txt");
        f.delete();
    }


    /**
     * 初始化查询对象
     */
    public void getRequestMap(){
        requestMap=new HashMap();
        if(VG.USERINFO!=null) {
            requestMap.put("loginId", VG.USERINFO.getId());
            requestMap.put("isAdmin", VG.USERINFO.getIsAdmin());
        }else{
            String str=readInFile();
            if(!str.equalsIgnoreCase("")) {
                Type listType = new TypeToken<UserInfo>() {}.getType();
                UserInfo userInfo=gson.fromJson(str, listType);
                VG.USERINFO = gson.fromJson(str, listType);
                requestMap.put("loginId", VG.USERINFO.getId());
                requestMap.put("isAdmin", VG.USERINFO.getIsAdmin());
            }
        }
    }
}

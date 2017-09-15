package com.example.administrator.myhappyform;

import android.app.DatePickerDialog;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;

import android.view.View;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myhappyform.entity.UserInfo;
import com.example.administrator.myhappyform.util.BaseActivity;
import com.example.administrator.myhappyform.util.OkManager;
import com.example.administrator.myhappyform.util.VG;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/15.
 */

public class AddKqActivity extends BaseActivity {
    public  int year,month,day;
    EditText sgxm;//施工项目
    EditText sgqy;//施工区域
    EditText staffname;//施工人员
    EditText workdate;//施工日期
    EditText workduringtime;//出勤时间
    EditText overtime;//加班时间
    Spinner spinner;//所属区域
    EditText workcontent;//具体内容
    EditText remark;//备注
    Button button;
    private OkManager manager;
    private JSONObject returnObj;
    public View view;
    DatePickerDialog datePickerDialog;
    public Map requestmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addkq);
        setTitle("考勤信息(录入)");
        Calendar calendar=Calendar.getInstance();
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);
        day=calendar.get(Calendar.DAY_OF_MONTH);
        workdate=(EditText) view.findViewById(R.id.workdate);
        workdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                if(datePickerDialog!=null) datePickerDialog.hide();
                datePickerDialog = new DatePickerDialog(AddKqActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                workdate.setText(year + "-" + monthOfYear + "-"
                                        + dayOfMonth);
                            }
                        }, calendar.get(Calendar.YEAR), calendar
                        .get(Calendar.MONTH), calendar
                        .get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();

            }
        });
        spinner= (Spinner)view.findViewById(R.id.departmentname);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener(){
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String result=adapterView.getItemAtPosition(i).toString();
                Log.i("spinner",result);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        button=(Button)view.findViewById(R.id.button1);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveInfo();
            }
        });
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();

        String id=(String)bundle.get("id");
        if(!id.equalsIgnoreCase("")) {
            requestmap=new HashMap();
            requestmap.put("id",id);
            initData();
        }
    }
    public void initData(){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // button3.setText("已经改变了哦");

                manager = OkManager.getInstance();
                manager.sendComplexForm("", requestmap, new OkManager.returnJson() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        try {
                            boolean flag;
                            flag=(Boolean)jsonObject.get("msg");
                            if(flag){
                                Toast.makeText(AddKqActivity.this,"初始化成功",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(AddKqActivity.this,"初始化错误",Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


            }
        });

    }

    void saveInfo(){
        sgxm=(EditText)view.findViewById(R.id.sgxm) ;//施工项目
        sgqy=(EditText)view.findViewById(R.id.sgqy);//施工区域
        staffname=(EditText)view.findViewById(R.id.staffname);//施工人员
        workdate=(EditText)view.findViewById(R.id.workdate);//施工日期
        workduringtime=(EditText)view.findViewById(R.id.workduringtime);//出勤时间
        overtime=(EditText)view.findViewById(R.id.overtime);//加班时间
        String ssqy=spinner.getSelectedItem().toString();//所属区域
        workcontent=(EditText)view.findViewById(R.id.workcontent);//具体内容
        remark=(EditText)view.findViewById(R.id.remark);//备注
        TextView id=(TextView)view.findViewById(R.id.id);
        Map map =new HashMap<>();
        map.put("sgxm",sgxm.getText().toString());
        map.put("sgqy",sgqy.getText().toString());
        map.put("workdate",workdate.getText().toString());
        map.put("staffname",staffname.getText().toString());
        map.put("workduringtime",workduringtime.getText().toString());
        map.put("overtime",overtime.getText().toString());
        map.put("workcontent",workcontent.getText().toString());
        map.put("remark",remark.getText().toString());
        map.put("departmentname",ssqy);
        map.put("id",id.getText().toString());
        map.put("loginId", VG.USERINFO.getId());
        map.put("isAdmin",VG.USERINFO.getIsadmin());
        openWaiting();
        manager = OkManager.getInstance();
        manager.sendComplexForm(VG.SAVE_CHECKINFO_PATH, map, new OkManager.returnJson() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                returnObj=jsonObject;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // button3.setText("已经改变了哦");
                        closeWaiting();
                        try {
                            boolean b=(Boolean) returnObj.get("msg");
                            if(b){
                                Toast.makeText(AddKqActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(AddKqActivity.this,"保存失败",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }
    public void show(int year,int month,int day){
        Toast.makeText(AddKqActivity.this,year+" "+(month+1)+" "+day,Toast.LENGTH_SHORT).show();
    }


    public   void  setTitle(String title){
        TextView textView=(TextView)findViewById(R.id.zy);
        textView.setText(title);
    }
}

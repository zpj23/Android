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

import com.example.administrator.myhappyform.entity.CheckInfo;
import com.example.administrator.myhappyform.util.BaseActivity;
import com.example.administrator.myhappyform.util.OkManager;
import com.example.administrator.myhappyform.util.VG;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Administrator on 2017/9/15.
 */

public class AddKqActivity extends BaseActivity {
    public  int year,month,day;
    public TextView hideView;
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
    DatePickerDialog datePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addkq);
        currentContext=AddKqActivity.this;
        setTitle("考勤信息(录入)");

        Calendar calendar=Calendar.getInstance();
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);
        day=calendar.get(Calendar.DAY_OF_MONTH);
        workdate=(EditText) findViewById(R.id.workdate);
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

                                workdate.setText(year + "-" + (monthOfYear+1) + "-"
                                        + dayOfMonth);
                            }
                        }, calendar.get(Calendar.YEAR), calendar
                        .get(Calendar.MONTH), calendar
                        .get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();

            }
        });
        spinner= (Spinner)findViewById(R.id.departmentname);
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

        button=(Button)findViewById(R.id.button1);
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
            getRequestMap();//初始化requestMap对象
            requestMap.put("id",id);
            initData();
        }
    }
    protected void initData(){;
            openWaiting();
            manager = OkManager.getInstance();
            manager.sendComplexForm(VG.FIND_CHECKINFO_BYID_PATH, requestMap, new OkManager.returnJson() {
                @Override
                public void onResponse(JSONObject jsonObject) {
                    try {
                        boolean flag;
                        flag=(Boolean)jsonObject.get("msg");
                        if(flag){
                            JSONObject job=(JSONObject)jsonObject.get("data");
                            Type listType = new TypeToken<CheckInfo>() {}.getType();
                            CheckInfo checkInfo= gson.fromJson(job.toString(),  listType);
                            fillData(checkInfo);
                            Toast.makeText(AddKqActivity.this,"初始化成功",Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(AddKqActivity.this,"初始化错误",Toast.LENGTH_SHORT).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }finally {
                        closeWaiting();
                    }
                }
            });
    }

    /**
     * 填充数据
     * @param checkInfo
     */
    private void fillData(CheckInfo checkInfo){
        hideView=(TextView)findViewById(R.id.id);
        hideView.setText(checkInfo.getId());
        sgxm=(EditText)findViewById(R.id.sgxm) ;//施工项目
        sgxm.setText(checkInfo.getSgxm());
        sgqy=(EditText)findViewById(R.id.sgqy);//施工区域
        sgqy.setText(checkInfo.getSgqy());
        staffname=(EditText)findViewById(R.id.staffname);//施工人员
        staffname.setText(checkInfo.getStaffname());
        workdate=(EditText)findViewById(R.id.workdate);//施工日期
        SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
        Date date=checkInfo.getWorkdate();
        String wd=sdf.format(checkInfo.getWorkdate());
        workdate.setText(wd);
        workduringtime=(EditText)findViewById(R.id.workduringtime);//出勤时间
        workduringtime.setText(checkInfo.getWorkduringtime()+"");
        overtime=(EditText)findViewById(R.id.overtime);//加班时间
        overtime.setText(checkInfo.getOvertime()+"");
        if(checkInfo.getDepartmentname().equalsIgnoreCase("舟山")){
            spinner.setSelection(0,true);
        }else if(checkInfo.getDepartmentname().equalsIgnoreCase("海通工地")){
            spinner.setSelection(1,true);
        }else if(checkInfo.getDepartmentname().equalsIgnoreCase("海新工地")){
            spinner.setSelection(2,true);
        }else if(checkInfo.getDepartmentname().equalsIgnoreCase("招商工地")){
            spinner.setSelection(3,true);
        }else if(checkInfo.getDepartmentname().equalsIgnoreCase("远舟船舶家俱")){
            spinner.setSelection(4,true);
        }else if(checkInfo.getDepartmentname().equalsIgnoreCase("大津重工")){
            spinner.setSelection(5,true);
        }else if(checkInfo.getDepartmentname().equalsIgnoreCase("振华")){
            spinner.setSelection(6,true);
        }
        workcontent=(EditText)findViewById(R.id.workcontent);//具体内容
        workcontent.setText(checkInfo.getWorkcontent());
        remark=(EditText)findViewById(R.id.remark);//备注
        remark.setText(checkInfo.getRemark());
    }

    /***
     * 保存数据
     */
    private void saveInfo(){
        sgxm=(EditText)findViewById(R.id.sgxm) ;//施工项目
        sgqy=(EditText)findViewById(R.id.sgqy);//施工区域
        staffname=(EditText)findViewById(R.id.staffname);//施工人员
        workdate=(EditText)findViewById(R.id.workdate);//施工日期
        workduringtime=(EditText)findViewById(R.id.workduringtime);//出勤时间
        overtime=(EditText)findViewById(R.id.overtime);//加班时间
        String ssqy=spinner.getSelectedItem().toString();//所属区域
        workcontent=(EditText)findViewById(R.id.workcontent);//具体内容
        remark=(EditText)findViewById(R.id.remark);//备注
        TextView id=(TextView)findViewById(R.id.id);
        getRequestMap();//初始化requestMap对象
        requestMap.put("sgxm",sgxm.getText().toString());
        requestMap.put("sgqy",sgqy.getText().toString());
        requestMap.put("workdate",workdate.getText().toString());
        requestMap.put("staffname",staffname.getText().toString());
        requestMap.put("workduringtime",workduringtime.getText().toString());
        requestMap.put("overtime",overtime.getText().toString());
        requestMap.put("workcontent",workcontent.getText().toString());
        requestMap.put("remark",remark.getText().toString());
        requestMap.put("departmentname",ssqy);
        requestMap.put("id",id.getText().toString());
        openWaiting();
        manager = OkManager.getInstance();
        manager.sendComplexForm(VG.SAVE_CHECKINFO_PATH, requestMap, new OkManager.returnJson() {

            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    boolean b=(Boolean) jsonObject.get("msg");
                    if(b){
                        Toast.makeText(AddKqActivity.this,"保存成功",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent();
                        Bundle bundle=new Bundle();
                        bundle.putString("date",workdate.getText().toString()+"");
                        intent.putExtras(bundle);
                        setResult(666,intent);
                        finish();
                    }else{
                        Toast.makeText(AddKqActivity.this,"保存失败",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    closeWaiting();
                }

            }
        });
    }

    /**
     * 保存成功跳转
     */
    public void saveSuccess(){

    }

    /**
     * 保存失败修改
     */
    public void saveFailed(){

    }
    public void show(int year,int month,int day){
        Toast.makeText(AddKqActivity.this,year+" "+(month+1)+" "+day,Toast.LENGTH_SHORT).show();
    }


    public   void  setTitle(String title){
        TextView textView=(TextView)findViewById(R.id.zy);
        textView.setText(title);
    }
}

package com.example.administrator.userinterfacedesign;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    public  int year,month,day;
    EditText address;//施工项目及区域
    EditText staffname;//施工人员
    EditText workdate;//施工日期
    EditText workduringtime;//出勤时间
    EditText overtime;//加班时间
    Spinner spinner;//所属区域
    EditText workcontent;//具体内容
    EditText remark;//备注

    Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        DatePicker dp=(DatePicker)findViewById(R.id.workdate);
        Calendar calendar=Calendar.getInstance();
        year=calendar.get(Calendar.YEAR);
        month=calendar.get(Calendar.MONTH);
        day=calendar.get(Calendar.DAY_OF_MONTH);
        workdate=(EditText) findViewById(R.id.workdate);
        workdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDataDialog();
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
                address=(EditText)findViewById(R.id.address) ;//施工项目及区域
                staffname=(EditText)findViewById(R.id.staffname);//施工人员
                workdate=(EditText)findViewById(R.id.workdate);//施工日期
                workduringtime=(EditText)findViewById(R.id.workduringtime);//出勤时间
                overtime=(EditText)findViewById(R.id.overtime);//加班时间
                String ssqy=spinner.getSelectedItem().toString();//所属区域
                workcontent=(EditText)findViewById(R.id.workcontent);//具体内容
                remark=(EditText)findViewById(R.id.remark);//备注
                Log.i("address",address.getText().toString());
                Log.i("staffname",staffname.getText().toString());
                Log.i("workdate",workdate.getText().toString());
                Log.i("workduringtime",workduringtime.getText().toString());
                Log.i("overtime",overtime.getText().toString());
                Log.i("ssqy",ssqy);
                Log.i("workcontent",workcontent.getText().toString());
                Log.i("remark",remark.getText().toString());

            }
        });
    }
    public void show(int year,int month,int day){
        Toast.makeText(this,year+" "+(month+1)+" "+day,Toast.LENGTH_SHORT).show();
    }
    /**
     * 显示日期选择器
     */
    private void showDataDialog() {
        /**
         * in a fullscreen DatePicker is the choice of parent theme:
         * 主题（不设置的话就是默认的 ，xiaomi mi 3c 是6.0系统，显示的就是日历样式的）
         * 设置方法是 AlertDialog.THEME_HOLO_LIGHT,
         * 直接new就可以了         DatePickerDialog(Context context, int themeResId, DatePickerDialog.OnDateSetListener listener, int year, int monthOfYear, int dayOfMonth)
         ** @deprecated *//*
    @Deprecated
    public static final int THEME_DEVICE_DEFAULT_DARK = 4;
    *//** @deprecated *//*
    @Deprecated
    public static final int THEME_DEVICE_DEFAULT_LIGHT = 5;
    *//** @deprecated *//*
    @Deprecated
    public static final int THEME_HOLO_DARK = 2;
    *//** @deprecated *//*
    @Deprecated
    public static final int THEME_HOLO_LIGHT = 3;
    *//** @deprecated *//*
    @Deprecated
    public static final int THEME_TRADITIONAL = 1;
*/
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(MainActivity.this, AlertDialog.THEME_HOLO_DARK, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                // TODO Auto-generated method stub
                int mYear = year;
                int mMonth = month;
                int mDay = day;
                //更新EditText控件日期 小于10加0
                workdate.setText(new StringBuilder()
                        .append(mYear)
                        .append("-")
                        .append((mMonth + 1) < 10 ? 0 +""+ (mMonth + 1) : (mMonth + 1)+"")
                        .append("-")
                        .append((mDay < 10) ? 0 + mDay : mDay));
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        //设置时间范围
//        dateDialog.getDatePicker().setMinDate(CalendarUtil.GetLastMonthDate().getTimeInMillis());
        datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
        datePickerDialog.show();
    }
}

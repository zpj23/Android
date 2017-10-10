package com.example.administrator.myhappyform;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myhappyform.entity.Group;
import com.example.administrator.myhappyform.entity.Item;
import com.example.administrator.myhappyform.util.BaseActivity;
import com.example.administrator.myhappyform.util.MyBaseExpandableListAdapter;
import com.example.administrator.myhappyform.util.OkManager;
import com.example.administrator.myhappyform.util.VG;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Administrator on 2017/9/14.
 */

public class ListKqActivity extends BaseActivity {
    private ArrayList<Group> gData = null;
    private ArrayList<ArrayList<Item>> iData = null;
    private ArrayList<Item> lData = null;
    private ExpandableListView exlist_kq;
    private MyBaseExpandableListAdapter myAdapter = null;
    private OkManager manager;
    private String Tag="请求列表信息";
    private List<String> groupName=new ArrayList<String>();
    private EditText search_workdate;
    private Button search_button;
    DatePickerDialog datePickerDialog;
    private Button add_button;

    private JSONArray jsonArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        manager=OkManager.getInstance();
        super.onCreate(savedInstanceState);
        currentContext=ListKqActivity.this;
        setContentView(R.layout.activity_list);
        setTitle("考勤信息列表");
        exlist_kq = (ExpandableListView) findViewById(R.id.exlist_kq);
        search_workdate=(EditText) findViewById(R.id.search_workdate);
        search_workdate.setInputType(InputType.TYPE_NULL);
        search_workdate.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                // TODO Auto-generated method stub
                if(hasFocus){
                    Calendar calendar = Calendar.getInstance();
                    if(datePickerDialog!=null) datePickerDialog.hide();
                    datePickerDialog = new DatePickerDialog(ListKqActivity.this,
                            new DatePickerDialog.OnDateSetListener() {

                                @Override
                                public void onDateSet(DatePicker view, int year,
                                                      int monthOfYear, int dayOfMonth) {

                                    search_workdate.setText(year + "-" + (monthOfYear+1) + "-"
                                            + dayOfMonth);
                                }
                            }, calendar.get(Calendar.YEAR), calendar
                            .get(Calendar.MONTH), calendar
                            .get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();
                }
            }
        });
        search_workdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                if(datePickerDialog!=null) datePickerDialog.hide();
                datePickerDialog = new DatePickerDialog(ListKqActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                search_workdate.setText(year + "-" + (monthOfYear+1) + "-"
                                        + dayOfMonth);
                            }
                        }, calendar.get(Calendar.YEAR), calendar
                        .get(Calendar.MONTH), calendar
                        .get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();

            }
        });
        //数据准备
        search_button=(Button)findViewById(R.id.search_info);
        search_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickSearchButton();
            }
        });
        add_button=(Button)findViewById(R.id.add_info);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent m=new Intent(ListKqActivity.this, AddKqActivity.class);
                Bundle bundle=new Bundle();
                bundle.putString("id","");
                m.putExtras(bundle);
                startActivityForResult(m,666);
            }
        });
        clickSearchButton();



    }

    private void  clickSearchButton(){
        openWaiting();
        getSearchTime();
        manager.sendComplexForm(VG.LIST_CHECKINFO_PATH,requestMap, new OkManager.returnJson() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {
                    Log.i(Tag,jsonObject.toString());
                    jsonArray=(JSONArray)jsonObject.get("list");
                    getGroupAndItemData(jsonArray);
                    myAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    closeWaiting();
                }
            }
        });
    }

    private void getSearchTime()  {
        Date date=null;
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        if(!search_workdate.getText().toString().equalsIgnoreCase("")){
            try {
                date=sdf.parse(search_workdate.getText().toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }else{
            date=new Date();
        }
        getRequestMap();//初始化requestMap对象
        requestMap.put("datemax",sdf.format(date));
        groupName=new ArrayList<String>();
        groupName.add(sdf.format(date));
        groupName.add(sdf.format(getSelfDate(date,-1)));
        groupName.add(sdf.format(getSelfDate(date,-2)));
        requestMap.put("datemin",sdf.format(getSelfDate(date,-2)));
    }
    private  Date getSelfDate(Date date,int n){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, n);
        date = calendar.getTime();
        return date;
    }


    private void getGroupAndItemData(JSONArray jarr) throws JSONException {
        gData = new ArrayList<Group>();
        iData = new ArrayList<ArrayList<Item>>();
        List<String> list= new ArrayList<String>();
        JSONObject obj;


            for(int n=0;n<groupName.size();n++){
                String category=groupName.get(n);
                gData.add(new Group(category));
                lData = new ArrayList<Item>();
                for(int k=0;k<jarr.length();k++){
                    obj=(JSONObject) jarr.get(k);
                    String wordate=((String)obj.get("workdate")).substring(0,10);
                    if(wordate.equalsIgnoreCase(groupName.get(n))){
                        lData.add(new Item((String)obj.get("id"),(String)obj.get("staffname")));
                    }
                }
                iData.add(lData);
            }

        myAdapter = new MyBaseExpandableListAdapter(gData,iData,ListKqActivity.this);
        exlist_kq.setAdapter(myAdapter);


        //为列表设置点击事件
        exlist_kq.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String pk=iData.get(groupPosition).get(childPosition).getCheck_info_id();
                initPopWindow(v,pk);
             //  toAddKq(iData.get(groupPosition).get(childPosition).getCheck_info_id());
                return true;
            }
            
        });

    }

    public   void  setTitle(String title){
        TextView textView=(TextView)findViewById(R.id.zy);
        textView.setText(title);
    }

    public void toAddKq(String id){
        Intent intent=new Intent(ListKqActivity.this,AddKqActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString("id",id);
        intent.putExtras(bundle);
        startActivityForResult(intent,666);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==666&&resultCode==666){
            Bundle bundle=data.getExtras();
            String time=(String)bundle.get("date");
            search_workdate.setText(time);
            clickSearchButton();
        }
    }

    /**
     * 点击一个item初始化popwindow
     * @param v
     */
    private void initPopWindow(View v,final String pk) {
        View view = LayoutInflater.from(currentContext).inflate(R.layout.item_popw, null, false);
        Button btn_xixi = (Button) view.findViewById(R.id.btn_bianji);
        Button btn_hehe = (Button) view.findViewById(R.id.btn_shanchu);
        //1.构造一个PopupWindow，参数依次是加载的View，宽高
        final PopupWindow popWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);

        popWindow.setAnimationStyle(R.anim.anim_pop);  //设置加载动画

        //这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的
        //代码的话，你会发现，当你把PopupWindow显示出来了，无论你按多少次后退键
        //PopupWindow并不会关闭，而且退不出程序，加上下述代码可以解决这个问题
        popWindow.setTouchable(true);
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        popWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));    //要为popWindow设置一个背景才有效


        //设置popupWindow显示的位置，参数依次是参照View，x轴的偏移量，y轴的偏移量
        popWindow.showAtLocation(v, Gravity.RIGHT, -10,10);

        //设置popupWindow里的按钮的事件
        btn_xixi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                toAddKq(pk);
                Toast.makeText(currentContext, "初始化信息，请稍后", Toast.LENGTH_SHORT).show();
                popWindow.dismiss();
            }
        });
        btn_hehe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                shanchuDialog(pk);
                popWindow.dismiss();
            }
        });
    }

    /**
     * 删除的弹出的对话框
     * @param id
     */
    private void shanchuDialog(final String id){
        new AlertDialog.Builder(currentContext)
                .setTitle("删除")
                .setMessage("确定要删除该条记录吗？")
                .setPositiveButton("是", new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        shanchuInfo(id);
                    }
                } )
                .setNegativeButton("否",  new DialogInterface.OnClickListener(){

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                } )
                .show();
    }

    /**
     * 删除方法
     * @param id
     */
    private void shanchuInfo(String id){
        openWaiting();
        getRequestMap();//初始化requestMap对象
        requestMap.put("delId",id);
        manager.sendComplexForm(VG.DELETE_CHECKINFO_BYID_PATH, requestMap, new OkManager.returnJson() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    Log.i(Tag,jsonObject.toString());
                    Boolean delFlag=(Boolean)jsonObject.get("msg");
                    if(delFlag){
                        Toast.makeText(currentContext, "删除成功，正在刷新列表", Toast.LENGTH_SHORT).show();
                        clickSearchButton();
                    }else{
                        Toast.makeText(currentContext, "删除失败", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }finally {
                    closeWaiting();
                }
            }
        });
    }
}

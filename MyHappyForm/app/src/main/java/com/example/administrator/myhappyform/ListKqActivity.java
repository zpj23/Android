package com.example.administrator.myhappyform;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ExpandableListView;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2017/9/14.
 */

public class ListKqActivity extends BaseActivity {
    private ArrayList<Group> gData = null;
    private ArrayList<ArrayList<Item>> iData = null;
    private ArrayList<Item> lData = null;
    private Context mContext;
    private ExpandableListView exlist_kq;
    private MyBaseExpandableListAdapter myAdapter = null;
    private OkManager manager;
    private String Tag="请求列表信息";
    private List<String> groupName=new ArrayList<String>();
    private EditText search_workdate;
    private Button search_button;
    DatePickerDialog datePickerDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        manager=OkManager.getInstance();
        super.onCreate(savedInstanceState);
        currentContext=ListKqActivity.this;
        setContentView(R.layout.activity_list);
        setTitle("考勤信息列表");
        mContext = ListKqActivity.this;
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
        clickSearchButton();
//        openWaiting();
//        manager.sendComplexForm(VG.LIST_CHECKINFO_PATH,requestMap, new OkManager.returnJson() {
//            @Override
//            public void onResponse(JSONObject jsonObject) {
//
//                try {
//                    Log.i(Tag,jsonObject.toString());
//                    JSONArray jsonArray=(JSONArray)jsonObject.get("list");
//                    getGroupAndItemData(jsonArray);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }finally {
//                    closeWaiting();
//                }
//            }
//        });
    }

    private void  clickSearchButton(){
        openWaiting();
        getSearchTime();
        manager.sendComplexForm(VG.LIST_CHECKINFO_PATH,requestMap, new OkManager.returnJson() {
            @Override
            public void onResponse(JSONObject jsonObject) {

                try {
                    Log.i(Tag,jsonObject.toString());
                    JSONArray jsonArray=(JSONArray)jsonObject.get("list");
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
//        Map map =new HashMap();
        Date date=null;

//        map.put("loginId",VG.USERINFO.getId());
//        map.put("isAdmin",VG.USERINFO.getIsAdmin());
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

        myAdapter = new MyBaseExpandableListAdapter(gData,iData,mContext);
        exlist_kq.setAdapter(myAdapter);


        //为列表设置点击事件
        exlist_kq.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                Toast.makeText(mContext, "你点击了：" + iData.get(groupPosition).get(childPosition).getiName(), Toast.LENGTH_SHORT).show();
                toAddKq(iData.get(groupPosition).get(childPosition).getCheck_info_id());
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
}

package com.example.administrator.myhappyform;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.example.administrator.myhappyform.fragment.ContentFragment;
import com.example.administrator.myhappyform.util.VG;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/6.
 */

public class MainActivity extends AppCompatActivity {
    private GridView gview;
    private List<Map<String, Object>> data_list;
    private SimpleAdapter sim_adapter;
    // 图片封装为一个数组
    private int[] icon = { R.drawable.kqgl1, R.drawable.xtgl};
    private String[] iconName = { "考勤", "系统"};

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
 //       Intent intent=getIntent();
//        TextView user=(TextView)findViewById(R.id.user);
//        TextView pwd=(TextView)findViewById(R.id.pwd);
//        user.setText("用户名："+intent.getExtras().get("username").toString());
//        try {
//            pwd.setText("密码："+ (String) VG.USERINFO.get("password"));
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        setDefaultFragment();
        initMenu();
    }
    public List<Map<String, Object>> getData(){
        //cion和iconName的长度是相同的，这里任选其一都可以
        for(int i=0;i<icon.length;i++){
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("image_img", icon[i]);
            map.put("text_tex", iconName[i]);
            data_list.add(map);
        }

        return data_list;
    }
    private void initMenu(){
        gview = (GridView) findViewById(R.id.gview);
        //新建List
        data_list = new ArrayList<Map<String, Object>>();
        //获取数据
        getData();
        //新建适配器
        String [] from ={"image_img","text_tex"};
        int [] to = {R.id.image_img,R.id.text_tex};
        /*SimpleAdapter的参数说明
		 * 第一个参数 表示访问整个android应用程序接口，基本上所有的组件都需要
		 * 第二个参数表示生成一个Map(String ,Object)列表选项
		 * 第三个参数表示界面布局的id  表示该文件作为列表项的组件
		 * 第四个参数表示该Map对象的哪些key对应value来生成列表项
		 * 第五个参数表示来填充的组件 Map对象key对应的资源一依次填充组件 顺序有对应关系
		 * 注意的是map对象可以key可以找不到 但组件的必须要有资源填充  因为 找不到key也会返回null 其实就相当于给了一个null资源
		 * 下面的程序中如果 new String[] { "name", "head", "desc","name" } new int[] {R.id.name,R.id.head,R.id.desc,R.id.head}
		 * 这个head的组件会被name资源覆盖
		 * */
        sim_adapter = new SimpleAdapter(this, data_list, R.layout.item, from, to);
        //配置适配器
        gview.setAdapter(sim_adapter);
    }

    private void setDefaultFragment()
    {
//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction transaction = fm.beginTransaction();
//        ContentFragment contentFragment = new ContentFragment();
//        transaction.replace(R.id.id_content, contentFragment);
//        transaction.commit();
//        initMenu();
    }
}

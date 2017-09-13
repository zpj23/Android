package com.example.administrator.myhappyform;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.administrator.myhappyform.util.OkManager;
import com.example.administrator.myhappyform.util.VG;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText username, password;
    private Button bt_username_clear;
    private Button bt_pwd_clear;
    private final static String Tag = LoginActivity.class.getSimpleName();
//    private Button forgive_pwd;
    private Button bt_pwd_eye;
    private Button login;
//    private Button register;
    private boolean isOpen = false;
    private OkManager manager;
    private JSONObject reJsonObject;
    private ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        try {
            if(VG.USERINFO!=null){
                String username=(String)VG.USERINFO.get("username");
                String password=(String)VG.USERINFO.get("password");
                Map map=new HashMap();
                map.put("username",username);
                map.put("password",password);
                openWaiting();
                if(username!=""&&password!=""){
                    excuteLogin(map);
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        initView();

    }

    private void initView() {

        username = (EditText) findViewById(R.id.username);
        // 监听文本框内容变化
        username.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // 获得文本框中的用户
                String user = username.getText().toString().trim();
                if ("".equals(user)) {
                    // 用户名为空,设置按钮不可见
                    bt_username_clear.setVisibility(View.INVISIBLE);
                } else {
                    // 用户名不为空，设置按钮可见
                    bt_username_clear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        password = (EditText) findViewById(R.id.password);
        // 监听文本框内容变化
        password.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                // 获得文本框中的用户
                String pwd = password.getText().toString().trim();
                if ("".equals(pwd)) {
                    // 用户名为空,设置按钮不可见
                    bt_pwd_clear.setVisibility(View.INVISIBLE);
                } else {
                    // 用户名不为空，设置按钮可见
                    bt_pwd_clear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        bt_username_clear = (Button) findViewById(R.id.bt_username_clear);
        bt_username_clear.setOnClickListener(this);

        bt_pwd_clear = (Button) findViewById(R.id.bt_pwd_clear);
        bt_pwd_clear.setOnClickListener(this);

        bt_pwd_eye = (Button) findViewById(R.id.bt_pwd_eye);
        bt_pwd_eye.setOnClickListener(this);

        login = (Button) findViewById(R.id.login);
        login.setOnClickListener(this);

//        register = (Button) findViewById(R.id.register);
//        register.setOnClickListener(this);

//        forgive_pwd = (Button) findViewById(R.id.forgive_pwd);
//        forgive_pwd.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_username_clear:
                // 清除登录名
                username.setText("");
                break;
            case R.id.bt_pwd_clear:
                // 清除密码
                password.setText("");
                break;
            case R.id.bt_pwd_eye:
                // 密码可见与不可见的切换
                if (isOpen) {
                    isOpen = false;
                } else {
                    isOpen = true;
                }

                // 默认isOpen是false,密码不可见
                changePwdOpenOrClose(isOpen);

                break;
            case R.id.login:
                // TODO 登录按钮
                login();
                break;
//            case R.id.register:
//                // 注册按钮
//                Toast.makeText(LoginActivity.this, "注册", Toast.LENGTH_SHORT).show();
//                break;
//            case R.id.forgive_pwd:
//                // 忘记密码按钮
//                Toast.makeText(LoginActivity.this, "忘记密码", Toast.LENGTH_SHORT).show();
//                break;

            default:
                break;
        }
    }
    /**
     * 登陆访问后台
     *
     *
     */
    private void login(){
        openWaiting();
        Map map=new HashMap();
        map.put("username",username.getText().toString());
        map.put("password",password.getText().toString());
        excuteLogin(map);
    }

    /**
     * 密码可见与不可见的切换
     *
     * @param flag
     */
    private void changePwdOpenOrClose(boolean flag) {
        // 第一次过来是false，密码不可见
        if (flag) {
            // 密码可见
            bt_pwd_eye.setBackgroundResource(R.drawable.eye_open);
            // 设置EditText的密码可见
            password.setTransformationMethod(HideReturnsTransformationMethod
                    .getInstance());
        } else {
            // 密码不接见
            bt_pwd_eye.setBackgroundResource(R.drawable.eye_close);
            // 设置EditText的密码隐藏
            password.setTransformationMethod(PasswordTransformationMethod
                    .getInstance());
        }
    }

    private void openWaiting(){
        pd = ProgressDialog.show(LoginActivity.this, "", "加载中，请稍后……");
    }
    private void closeWaiting(){
        pd.dismiss();
    }
    public void excuteLogin(Map map){
        manager = OkManager.getInstance();
        manager.sendComplexForm(VG.LOGIN_PATH, map, new OkManager.returnJson() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                Log.i(Tag, jsonObject.toString());
                reJsonObject=jsonObject;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // button3.setText("已经改变了哦");
                        closeWaiting();
                        boolean flag;
                        Bundle b=new Bundle();
                        try {
                            flag=(Boolean)reJsonObject.get("msg");
                            if(flag){
                                VG.USERINFO=(JSONObject)reJsonObject.get("data");
                                b.putString("username",(String)VG.USERINFO.get("username"));
                                Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                                intent.putExtras(b);
                                startActivity(intent);
                                LoginActivity.this.finish();
                            }else{
                                Toast.makeText(LoginActivity.this,"用户名或者密码错误",Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                });
            }
        });
    }
}

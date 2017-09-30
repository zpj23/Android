package com.example.administrator.myhappyform.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.example.administrator.myhappyform.AddKqActivity;
import com.example.administrator.myhappyform.ListKqActivity;
import com.example.administrator.myhappyform.LoginActivity;
import com.example.administrator.myhappyform.MainActivity;
import com.example.administrator.myhappyform.R;
import com.example.administrator.myhappyform.util.BaseActivity;
import com.example.administrator.myhappyform.util.VG;

import java.io.File;

/**
 * Created by Administrator on 2017/9/6.
 */

public class TitleFragment extends Fragment {

    private ImageButton mLeftMenu;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_title, container, false);
        mLeftMenu = (ImageButton) view.findViewById(R.id.id_title_left_btn);
        mLeftMenu.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(getActivity(),
                        "i am an ImageButton in TitleFragment ! ",
                        Toast.LENGTH_SHORT).show();

                PopupMenu popup = new PopupMenu(getActivity(),mLeftMenu);
                popup.getMenuInflater().inflate(R.menu.menu_pop, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()){
                            case R.id.xgmm:

                                Toast.makeText(getActivity(),"修改密码~",Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.tcxt:
                                Toast.makeText(getActivity(),"退出系统~",Toast.LENGTH_SHORT).show();
                                VG.isExit=true;
                                Intent intent=new Intent(getActivity(),LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish();
                                break;
                        }
                        return true;
                    }
                });
                popup.show();
            }
        });
        return view;
    }


}

package com.example.administrator.myhappyform.entity;

/**
 * Created by Jay on 2015/9/25 0025.
 */
public class Item {

//    private int iId;
    private String iName;
    private String check_info_id;

    public Item() {

    }

    public Item(String check_info_id, String iName) {
        this.check_info_id = check_info_id;
        this.iName = iName;
    }



    public String getiName() {
        return iName;
    }



    public void setiName(String iName) {
        this.iName = iName;
    }

    public String getCheck_info_id() {
        return check_info_id;
    }

    public void setCheck_info_id(String check_info_id) {
        this.check_info_id = check_info_id;
    }
}

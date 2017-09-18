package com.example.administrator.myhappyform.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by Administrator on 2017/9/13.
 */

public class CheckInfo implements Serializable{
    private static final long serialVersionUID = 7162329841877212054L;
    private String id;
    private String staffname;//员工名
    private Date workdate;//工作日期；
    private double workduringtime=8;//工作时长
    private String departmentname;//工作地点 对应部门
    private String departmentcode;//对应部门 编码
    private String workcontent;//工作内容
    private String adddate;//创建时间
    private String address;//施工项目及区域
    private double overtime;//加班时长
    private int createuserid;//创建人id
    private String remark;//备注
    private String shenhe;//审核功能 0待审核，1已审核
    private String sgxm;//address字段分出来的施工项目
    private String sgqy;//address字段分出来的施工区域

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStaffname() {
        return staffname;
    }

    public void setStaffname(String staffname) {
        this.staffname = staffname;
    }

    public Date getWorkdate() {
        return workdate;
    }

    public void setWorkdate(Date workdate) {
        this.workdate = workdate;
    }

    public double getWorkduringtime() {
        return workduringtime;
    }

    public void setWorkduringtime(double workduringtime) {
        this.workduringtime = workduringtime;
    }

    public String getDepartmentname() {
        return departmentname;
    }

    public void setDepartmentname(String departmentname) {
        this.departmentname = departmentname;
    }

    public String getDepartmentcode() {
        return departmentcode;
    }

    public void setDepartmentcode(String departmentcode) {
        this.departmentcode = departmentcode;
    }

    public String getWorkcontent() {
        return workcontent;
    }

    public void setWorkcontent(String workcontent) {
        this.workcontent = workcontent;
    }

    public String getAdddate() {
        return adddate;
    }

    public void setAdddate(String adddate) {
        this.adddate = adddate;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getOvertime() {
        return overtime;
    }

    public void setOvertime(double overtime) {
        this.overtime = overtime;
    }

    public int getCreateuserid() {
        return createuserid;
    }

    public void setCreateuserid(int createuserid) {
        this.createuserid = createuserid;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getShenhe() {
        return shenhe;
    }

    public void setShenhe(String shenhe) {
        this.shenhe = shenhe;
    }

    public String getSgxm() {
        return sgxm;
    }

    public void setSgxm(String sgxm) {
        this.sgxm = sgxm;
    }

    public String getSgqy() {
        return sgqy;
    }

    public void setSgqy(String sgqy) {
        this.sgqy = sgqy;
    }
}

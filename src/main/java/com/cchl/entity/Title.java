package com.cchl.entity;

import java.util.Date;

/**
 * 题目表
 */
public class Title {
    //编号
    private Integer id;
    //题目内容
    private String content;
    //题目介绍
    private String instruction;
    //状态，未通过审核为0，通过审核为1
    private Byte status;
    //创建时间
    private Date createTime;
    //选题总人数
    private int totalNumber;
    //已选人数
    private int hasSelect;
    //所属学院
    private Department department;

    @Override
    public String toString() {
        return "Title{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", instruction='" + instruction + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
                ", totalNumber=" + totalNumber +
                ", has_select=" + hasSelect +
                ", department=" + department +
                '}';
    }

    public int getTotalNumber() {
        return totalNumber;
    }

    public void setTotalNumber(int totalNumber) {
        this.totalNumber = totalNumber;
    }

    public int getHas_select() {
        return hasSelect;
    }

    public void setHasSelect(int hasSelect) {
        this.hasSelect = hasSelect;
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
package com.cchl.entity.vo;

import com.cchl.eumn.TimerType;

/**
 * 专业管理员发布定时任务
 */
public class VoTimer {
    //编号
    private int id;
    //定时任务面向的用户类型，0：学生  1：教师
    private int target;
    //类型定时任务类型
    private String type;
    //详情
    private String content;
    //开始时间
    private String begin;
    //结束时间
    private String end;
    //面向的学院编号，0代表所有学院
    private int department;

    public VoTimer() {
    }

    public VoTimer(int target, TimerType type, String content, String begin, String end, int department) {
        this.target = target;
        this.type = type.getType();
        this.content = content;
        this.begin = begin;
        this.end = end;
        this.department = department;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTarget() {
        return target;
    }

    public void setTarget(int target) {
        this.target = target;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public int getDepartment() {
        return department;
    }

    public void setDepartment(int department) {
        this.department = department;
    }
}

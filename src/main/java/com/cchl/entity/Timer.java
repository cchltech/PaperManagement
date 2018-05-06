package com.cchl.entity;

import java.util.Date;

/**
 * 时间表，用户专业管理员分时段设置任务
 */
public class Timer {
    //编号
    private Integer id;
    //内容
    private String content;
    //开始时间
    private Date beginTime;
    //结束时间
    private Date endTime;
    //创建时间
    private Date createTime;
    //面向的用户类型
    private Byte targetType;

    @Override
    public String toString() {
        return "Timer{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", beginTime=" + beginTime +
                ", endTime=" + endTime +
                ", createTime=" + createTime +
                ", targetType=" + targetType +
                '}';
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

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Byte getTargetType() {
        return targetType;
    }

    public void setTargetType(Byte targetType) {
        this.targetType = targetType;
    }
}
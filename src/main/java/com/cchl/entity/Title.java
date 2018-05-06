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

    @Override
    public String toString() {
        return "Title{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", instruction='" + instruction + '\'' +
                ", status=" + status +
                ", createTime=" + createTime +
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
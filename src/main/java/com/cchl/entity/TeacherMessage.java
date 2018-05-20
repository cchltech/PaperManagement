package com.cchl.entity;

import java.util.Date;

/**
 * 教师内容实体
 */
public class TeacherMessage {

    //消息内容
    private String content;
    //创建时间
    private Date createTime;
    //版本号
    private Integer version;

    @Override
    public String toString() {
        return "TeacherMessage{" +
                "content='" + content + '\'' +
                ", createTime=" + createTime +
                ", version=" + version +
                '}';
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}

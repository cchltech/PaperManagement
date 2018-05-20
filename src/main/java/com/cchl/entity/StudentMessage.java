package com.cchl.entity;

import java.util.Date;

/**
 * 学生消息内容实体
 */
public class StudentMessage {

    //消息内容
    private String content;
    //创建时间
    private Date createTime;
    //学院id
    private Integer departmentId;
    //版本号
    private Integer version;

    @Override
    public String toString() {
        return "StudentMessage{" +
                "content='" + content + '\'' +
                ", createTime=" + createTime +
                ", departmentId=" + departmentId +
                ", version=" + version +
                '}';
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
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
}

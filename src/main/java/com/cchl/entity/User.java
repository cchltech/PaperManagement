package com.cchl.entity;

import java.util.Date;

/**
 * 用户表
 */
public class User {
    //编号
    private Integer id;
    //状态
    private Byte status;
    //类型，学生为0，教师为1
    private Byte type;
    //创建时间
    private Date createTime;
    //添加学生实体
    private Student student;
    //添加教师实体
    private Teacher teacher;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", status=" + status +
                ", type=" + type +
                ", createTime=" + createTime +
                ", student=" + student +
                ", teacher=" + teacher +
                '}';
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Byte getStatus() {
        return status;
    }

    public void setStatus(Byte status) {
        this.status = status;
    }

    public Byte getType() {
        return type;
    }

    public void setType(Byte type) {
        this.type = type;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

}
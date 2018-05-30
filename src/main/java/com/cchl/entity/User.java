package com.cchl.entity;

import java.text.SimpleDateFormat;
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

    //名字
    private String name;
    //学号/工号
    private long number;
    //学院
    private String departmentName;
    //身份
    private String identity;
    //注册时间
    private String registerDate;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", status=" + status +
                ", type=" + type +
                ", createTime=" + createTime +
                ", student=" + student +
                ", teacher=" + teacher +
                ", name='" + name + '\'' +
                ", number=" + number +
                ", departmentName='" + departmentName + '\'' +
                ", identity='" + identity + '\'' +
                ", registerDate='" + registerDate + '\'' +
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getNumber() {
        return number;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }

    public String getIdentity() {
        return identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getRegisterDate() {
        return registerDate;
    }

    public void setRegisterDate(String registerDate) {
        this.registerDate = registerDate;
    }

    public void happyGive () {
        if(type == 1){
            this.name = this.teacher.getName();
            this.number = this.teacher.getId();
            this.identity = "教师";
            this.departmentName = this.teacher.getDepartment().getName();
            this.registerDate = switchTime();
        }
        else {
            this.name = this.student.getName();
            this.number = this.student.getId();
            this.identity = "学生";
            this.departmentName = this.student.getDepartment().getName();
            this.registerDate = switchTime();
        }
    }
    public String switchTime() {
        SimpleDateFormat format =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return format.format(this.createTime);
    }
}
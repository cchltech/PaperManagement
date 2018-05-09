package com.cchl.entity;

/**
 * 学生表
 */
public class Student {
    //学号
    private Long id;
    //姓名
    private String name;
    //密码
    private String password;
    //性别，女生为1，男生为1
    private Byte sex;
    //邮箱
    private String email;
    //学院id
    private Integer departmentId;
    //年级
    private Byte grade;
    //专业id
    private Integer majorId;
    //账户id
    private Integer userId;
    //电话
    private Long phone;
    //关联学院表
    private Department department;
    //关联专业表
    private Major major;
    //关联账户表
    private User user;

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", password='" + password + '\'' +
                ", sex=" + sex +
                ", email='" + email + '\'' +
                ", departmentId=" + departmentId +
                ", grade=" + grade +
                ", majorId=" + majorId +
                ", userId=" + userId +
                ", phone=" + phone +
                ", department=" + department +
                ", major=" + major +
                ", user=" + user +
                '}';
    }

    public Department getDepartment() {
        return department;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public Major getMajor() {
        return major;
    }

    public void setMajor(Major major) {
        this.major = major;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Byte getSex() {
        return sex;
    }

    public void setSex(Byte sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(Integer departmentId) {
        this.departmentId = departmentId;
    }

    public Byte getGrade() {
        return grade;
    }

    public void setGrade(Byte grade) {
        this.grade = grade;
    }

    public Integer getMajorId() {
        return majorId;
    }

    public void setMajorId(Integer majorId) {
        this.majorId = majorId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
    }
}
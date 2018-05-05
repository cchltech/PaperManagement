package com.cchl.entity;

/**
 * 管理员信息实体类
 */
public class AdminInfo {

    //编号
    private Integer id;
    //权限
    private Byte power;
    //账户id
    private Integer userId;
    //账户关联
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Byte getPower() {
        return power;
    }

    public void setPower(Byte power) {
        this.power = power;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }
}
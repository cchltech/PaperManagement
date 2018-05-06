package com.cchl.entity;

/**
 * 账户与论文计划关联的表
 */
public class UserPaper {
    //编号
    private Integer id;
    //账户实体
    private User user;
    //论文计划实体
    private PaperPlan paperPlan;

    @Override
    public String toString() {
        return "UserPaper{" +
                "id=" + id +
                ", user=" + user +
                ", paperPlan=" + paperPlan +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public PaperPlan getPaperPlan() {
        return paperPlan;
    }

    public void setPaperPlan(PaperPlan paperPlan) {
        this.paperPlan = paperPlan;
    }
}

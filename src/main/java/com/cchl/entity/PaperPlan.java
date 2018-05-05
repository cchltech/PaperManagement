package com.cchl.entity;

import java.util.Date;

/**
 * 论文计划表
 */
public class PaperPlan {
    //编号
    private Integer id;
    //创建时间
    private Date createTime;
    //所属题目编号
    private Integer titleId;
    //关联题目表
    private Title title;

    public Title getTitle() {
        return title;
    }

    public void setTitle(Title title) {
        this.title = title;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getTitleId() {
        return titleId;
    }

    public void setTitleId(Integer titleId) {
        this.titleId = titleId;
    }
}
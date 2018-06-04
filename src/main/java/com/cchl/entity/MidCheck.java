package com.cchl.entity;

/**
 * 中期检查表
 */
public class MidCheck {
    //编号
    private Integer id;
    //文件路径
    private String filePath;
    //论文计划id
    private Integer paperPlanId;
    //关联论文计划
    private PaperPlan paperPlan;
    //创建时间
    private String createTime;

    @Override
    public String toString() {
        return "MidCheck{" +
                "id=" + id +
                ", filePath='" + filePath + '\'' +
                ", paperPlan=" + paperPlan +
                '}';
    }

    public PaperPlan getPaperPlan() {
        return paperPlan;
    }

    public void setPaperPlan(PaperPlan paperPlan) {
        this.paperPlan = paperPlan;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Integer getPaperPlanId() {
        return paperPlanId;
    }

    public void setPaperPlanId(Integer paperPlanId) {
        this.paperPlanId = paperPlanId;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }
}
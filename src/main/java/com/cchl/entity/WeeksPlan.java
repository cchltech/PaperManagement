package com.cchl.entity;

/**
 * 周计划
 */
public class WeeksPlan {
    //编号
    private Integer id;
    //文件路径
    private String filePath;
    //论文计划编号
    private Integer paperPlanId;
    //论文计划实体
    private PaperPlan paperPlan;

    @Override
    public String toString() {
        return "WeeksPlan{" +
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
}
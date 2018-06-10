package com.cchl.entity;

/**
 * 论文实体类
 */
public class Paper {
    //编号
    private Integer id;
    //文件路径
    private String filePath;
    //分数
    private Integer score;
    //论文计划id
    private Integer paperPlanId;
    //关联论文计划
    private PaperPlan paperPlan;
    //创建时间
    private String createTime;
    //评价
    private String content;

    @Override
    public String toString() {
        return "Paper{" +
                "id=" + id +
                ", filePath='" + filePath + '\'' +
                ", score=" + score +
                ", paperPlanId=" + paperPlanId +
                ", paperPlan=" + paperPlan +
                ", createTime='" + createTime + '\'' +
                ", content='" + content + '\'' +
                '}';
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
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
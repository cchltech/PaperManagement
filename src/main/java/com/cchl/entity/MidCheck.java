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
    //状态信息 0：未通过  1：通过
    private byte status;

    @Override
    public String toString() {
        return "MidCheck{" +
                "id=" + id +
                ", filePath='" + filePath + '\'' +
                ", paperPlanId=" + paperPlanId +
                ", paperPlan=" + paperPlan +
                ", createTime='" + createTime + '\'' +
                ", status=" + status +
                '}';
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
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
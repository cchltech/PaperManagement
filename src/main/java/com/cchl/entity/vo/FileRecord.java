package com.cchl.entity.vo;

/**
 * 文件记录
 */
public class FileRecord {

    private Integer paperPlanId;

    private String type;

    private String name;

    private String createTime;

    public FileRecord(String type, String name, String createTime) {
        this.type = type;
        this.name = name;
        this.createTime = createTime;
    }

    public FileRecord(Integer paperPlanId, String type, String name, String createTime) {
        this.paperPlanId = paperPlanId;
        this.type = type;
        this.name = name;
        this.createTime = createTime;
    }

    public FileRecord() {
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public Integer getPaperPlanId() {
        return paperPlanId;
    }

    public void setPaperPlanId(Integer paperPlanId) {
        this.paperPlanId = paperPlanId;
    }
}

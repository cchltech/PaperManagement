package com.cchl.entity.vo;

/**
 * 文件记录
 */
public class FileRecord {

    private String type;

    private String name;

    private String createTime;

    public FileRecord(String type, String name, String createTime) {
        this.type = type;
        this.name = name;
        this.createTime = createTime;
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
}

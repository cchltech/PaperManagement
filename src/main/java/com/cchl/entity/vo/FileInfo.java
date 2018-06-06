package com.cchl.entity.vo;

import com.cchl.entity.Student;

public class FileInfo {
    //学号/工号
    private Long id;
    //姓名
    private String name;
    //专业，如果需要的话
    private String major;
    //文件编号
    private Integer fileId;
    //文件名
    private String fileName;
    //上传时间
    private String time;

    public FileInfo() {
    }

    @Override
    public String toString() {
        return "FileInfo{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", major='" + major + '\'' +
                ", fileId=" + fileId +
                ", fileName='" + fileName + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public Integer getFileId() {
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
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

    public String getMajor() {
        return major;
    }

    public void setMajor(String major) {
        this.major = major;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}

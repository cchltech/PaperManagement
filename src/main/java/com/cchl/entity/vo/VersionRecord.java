package com.cchl.entity.vo;

/**
 * 消息版本记录
 * 一开始消息版本是记录在最新插入的消息中的
 * 但当消息被删除是，版本就出错了
 * 所以专门写一个版本记录的类
 * 每次插入消息时根据这个类的版本记录确定消息的版本
 */
public class VersionRecord {

    //版本号
    private int version;
    //学院号
    private int departmentId;
    //面向的对象类型, 0:学生  1：教师
    private int type;

    public VersionRecord(int version, int departmentId, int type) {
        this.version = version;
        this.departmentId = departmentId;
        this.type = type;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

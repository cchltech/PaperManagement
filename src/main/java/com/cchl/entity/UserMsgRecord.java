package com.cchl.entity;

/**
 * 不同用户的消息浏览记录实体
 */
public class UserMsgRecord {
    //将用户id设置为主键
    private int id;
    //类型， 0：学生  1：教师
    private int type;
    //版本号
    private int version;
    //学院id，如果是学生的话还需要记录学院编号，通过学院编号查找消息
    private int departmentId;

    @Override
    public String toString() {
        return "UserMsgRecord{" +
                "id=" + id +
                ", type=" + type +
                ", version=" + version +
                ", departmentId=" + departmentId +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
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
}

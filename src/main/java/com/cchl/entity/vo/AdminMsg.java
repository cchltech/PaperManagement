package com.cchl.entity.vo;

/**
 * 管理院需要接收的信息
 */
public class AdminMsg {
    public enum TYPE{
        TITLE("title"),
        USER("user"),
        FILE("file")
        ;
        private String type;
        TYPE(String type) {
            this.type = type;
        }
        public String getType() {
            return type;
        }
    }
    //类型
    private String type;
    //未读消息总数，查看后就置零
    private int number;
    //所属学院
    private int departmentId;

    public AdminMsg() {
    }

    public AdminMsg(AdminMsg.TYPE type, int number, int departmentId) {
        this.type = type.getType();
        this.number = number;
        this.departmentId = departmentId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }
}

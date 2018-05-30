package com.cchl.eumn;

public enum TimerType {
    //提交题目审核表
    TITLE(0, "提交题目审核表"),
    //选题
    CHOICE_TITLE(1,"学生选题"),
    ;
    //类型编号
    private int code;
    //类型
    private String type;

    TimerType(int code, String type) {
        this.code = code;
        this.type = type;
    }

    public static TimerType stateOf(int type) {
        for (TimerType type1 : values()) {
            if (type1.getCode() == type)
                return type1;
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}

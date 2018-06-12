package com.cchl.eumn;

public enum Dictionary {

    SUCCESS(true, "操作成功"),
    DATA_LOST(false, "缺少信息"),
    SYSTEM_ERROR(false, "系统异常"),
    DATA_INSERT_FAIL(false, "数据插入失败"),
    UNKNOWN_IDENTITY(false, "未知身份"),
    NO_MORE_DATA(true, "没有更多数据了"),
    SUBMIT_FAIL(false, "抱歉，提交失败"),
    ILLEGAL(false, "非法操作"),
    NUMBER_IS_FULL(false, "抱歉，该选题人数已满"),
    ILLEGAL_VISIT(false, "非法访问"),
    NOT_BEGINNING(false, "不在选题时段内"),
    LOGIN_FAIL(false, "登录失败，请检查您的用户名和密码"),
    REPEAT_CHOICE(false, "请勿重复选题")
    ;

    Dictionary(boolean success, String msg) {
        this.success = success;
        this.msg = msg;
    }

    //结果
    private boolean success;
    //信息
    private String msg;

    public String getMsg() {
        return msg;
    }

    public boolean isSuccess() {
        return success;
    }

}

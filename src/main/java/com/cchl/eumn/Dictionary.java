package com.cchl.eumn;

public enum Dictionary {

    SUCCESS(true,"操作成功"),
    DATA_LOST(false, "缺少信息"),
    SYSTEM_ERROR(false, "系统异常"),
    DATA_INSERT_FAIL(false, "数据插入失败"),
    UNKNOWN_IDENTITY(false, "未知身份")
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

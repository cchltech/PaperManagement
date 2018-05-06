package com.cchl.eumn;

public enum Dictionary {

    SUCCESS(0,"操作成功"),
    DATA_LOST(1, "缺少信息"),
    ;

    Dictionary(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    //代码
    private int code;
    //信息
    private String msg;

    public String getMsg() {
        return msg;
    }
}

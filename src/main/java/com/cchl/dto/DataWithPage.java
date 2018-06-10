package com.cchl.dto;

import com.cchl.eumn.Dictionary;

/**
 * 带页数的实体类型
 */
public class DataWithPage<T> {

    private int code;
    //错误信息
    private String msg;
    //总页数
    private int count;
    //数据
    private T data;

    public DataWithPage(Dictionary dictionary) {
        if (!dictionary.isSuccess())
            this.code = 1;
        this.msg = dictionary.getMsg();
    }

    public DataWithPage(int code, int count, T data) {
        this.code = code;
        this.count = count;
        this.data = data;
    }

    public DataWithPage(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

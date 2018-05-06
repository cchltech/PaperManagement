package com.cchl.dto;

import com.cchl.eumn.Dictionary;

public class Result<T> {
    //返回状态
    private boolean success;
    //返回信息
    private String message;
    //返回实体
    private T data;

    public Result(boolean success, T data) {
        this.success = success;
        this.data = data;
    }

    public Result(boolean success, Dictionary dictionary) {
        this.success = success;
        this.message = dictionary.getMsg();
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

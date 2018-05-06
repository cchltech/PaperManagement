package com.cchl.execption;

/**
 * 系统异常
 */
public class SystemExecption extends RuntimeException {

    public SystemExecption() {
    }

    public SystemExecption(String message) {
        super(message);
    }
}

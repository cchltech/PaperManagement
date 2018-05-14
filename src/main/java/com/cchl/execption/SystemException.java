package com.cchl.execption;

import com.cchl.eumn.Dictionary;

/**
 * 系统异常
 */
public class SystemException extends RuntimeException {

    public SystemException() {
    }

    public SystemException(String message) {
        super(message);
    }

    public SystemException(Dictionary dictionary) {
        super(dictionary.getMsg());
    }
}

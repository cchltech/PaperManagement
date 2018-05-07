package com.cchl.execption;

import com.cchl.eumn.Dictionary;

/**
 * 未知身份异常
 */
public class UnknownIdentity extends SystemException {
    public UnknownIdentity(Dictionary dictionary) {
        super(dictionary);
    }
}

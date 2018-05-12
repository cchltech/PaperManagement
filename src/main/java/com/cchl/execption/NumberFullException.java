package com.cchl.execption;

import com.cchl.eumn.Dictionary;

public class NumberFullException extends SystemException {

    public NumberFullException() {
    }

    public NumberFullException(Dictionary dictionary) {
        super(dictionary);
    }
}

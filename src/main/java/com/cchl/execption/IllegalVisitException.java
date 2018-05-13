package com.cchl.execption;

import com.cchl.eumn.Dictionary;

public class IllegalVisitException extends SystemException {
    public IllegalVisitException() {
    }

    public IllegalVisitException(Dictionary dictionary) {
        super(dictionary);
    }
}

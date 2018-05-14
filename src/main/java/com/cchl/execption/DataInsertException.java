package com.cchl.execption;

import com.cchl.eumn.Dictionary;

public class DataInsertException extends SystemException {

    public DataInsertException(String msg) {
        super(msg);
    }

    public DataInsertException(Dictionary dictionary) {
        super(dictionary);
    }
}

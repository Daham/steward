package com.adamant.steward.exception;

import com.adamant.steward.exception.enums.ErrorCode;

public class DataSavingException extends DetailAddableException {

    public DataSavingException() {
        super(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}

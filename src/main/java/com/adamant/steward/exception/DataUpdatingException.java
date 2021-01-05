package com.adamant.steward.exception;

import com.adamant.steward.exception.enums.ErrorCode;

public class DataUpdatingException extends DetailAddableException {

    public DataUpdatingException() {
        super(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}

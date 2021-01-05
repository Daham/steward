package com.adamant.steward.exception;

import com.adamant.steward.exception.enums.ErrorCode;

public class DataLoadingException extends DetailAddableException {

    public DataLoadingException(String message) {
        super(message, ErrorCode.EXTERNAL_SERVICE_ERROR);
    }

    public DataLoadingException() {
        super(ErrorCode.EXTERNAL_SERVICE_ERROR);
    }
}
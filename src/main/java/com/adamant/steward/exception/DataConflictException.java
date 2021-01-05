package com.adamant.steward.exception;

import com.adamant.steward.exception.enums.ErrorCode;

public class DataConflictException extends DetailAddableException {

    public DataConflictException() {
        super(ErrorCode.CONFLICT);
    }
}

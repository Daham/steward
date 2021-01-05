package com.adamant.steward.exception;

import com.adamant.steward.exception.enums.ErrorCode;

public class DataRemovalException extends DetailAddableException {

    public DataRemovalException() {
        super(ErrorCode.INTERNAL_SERVER_ERROR);
    }
}

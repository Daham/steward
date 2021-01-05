package com.adamant.steward.exception;

import com.adamant.steward.exception.enums.ErrorCode;

public class UnprocessableEntityException extends DetailAddableException {

    public UnprocessableEntityException() {
        super(ErrorCode.UNPROCESSABLE_ENTITY);
    }
}

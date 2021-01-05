package com.adamant.steward.exception;

import com.adamant.steward.exception.enums.ErrorCode;
import lombok.Getter;

@Getter
public class ValidationException extends DetailAddableException {

    public ValidationException(String message) {
        super(message, ErrorCode.VALIDATION_ERROR);
    }
}


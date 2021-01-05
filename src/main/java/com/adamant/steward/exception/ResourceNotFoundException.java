package com.adamant.steward.exception;

import com.adamant.steward.exception.enums.ErrorCode;

public class ResourceNotFoundException extends DetailAddableException {

    public ResourceNotFoundException() {
        super(ErrorCode.RESOURCE_NOT_FOUND);
    }

}


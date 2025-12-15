package com.minsu.webservice.global.exception;

import com.minsu.webservice.global.error.ErrorCode;

import java.util.Map;

public class ApiException extends RuntimeException {

    private final ErrorCode errorCode;
    private final Map<String, Object> details;

    public ApiException(ErrorCode errorCode) {
        super(errorCode.message());
        this.errorCode = errorCode;
        this.details = null;
    }

    public ApiException(ErrorCode errorCode, Map<String, Object> details) {
        super(errorCode.message());
        this.errorCode = errorCode;
        this.details = details;
    }

    public ErrorCode getErrorCode() { return errorCode; }
    public Map<String, Object> getDetails() { return details; }
}

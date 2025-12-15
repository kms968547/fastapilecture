package com.minsu.webservice.global.error;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    AUTH_INVALID_CREDENTIALS(HttpStatus.UNAUTHORIZED, "AUTH_INVALID_CREDENTIALS", "Invalid credentials"),

    REFRESH_TOKEN_INVALID(HttpStatus.UNAUTHORIZED, "REFRESH_TOKEN_INVALID", "Invalid refresh token"),
    REFRESH_TOKEN_REVOKED(HttpStatus.UNAUTHORIZED, "REFRESH_TOKEN_REVOKED", "Refresh token revoked"),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "REFRESH_TOKEN_EXPIRED", "Refresh token expired"),

    USER_DUPLICATE_EMAIL(HttpStatus.CONFLICT, "USER_DUPLICATE_EMAIL", "Duplicate email"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_NOT_FOUND", "User not found"),
    USER_DEACTIVATED(HttpStatus.FORBIDDEN, "USER_DEACTIVATED", "User account is deactivated"),
    BOOK_NOT_FOUND(HttpStatus.NOT_FOUND, "BOOK_NOT_FOUND", "Book not found"),
    REVIEW_NOT_FOUND(HttpStatus.NOT_FOUND, "REVIEW_NOT_FOUND", "Review not found"),

    ORDER_NOT_FOUND(HttpStatus.NOT_FOUND, "ORDER_NOT_FOUND", "Order not found"),
    INSUFFICIENT_STOCK(HttpStatus.BAD_REQUEST, "INSUFFICIENT_STOCK", "Insufficient book stock"),

    MALFORMED_JSON(HttpStatus.BAD_REQUEST, "MALFORMED_JSON", "Malformed JSON"),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "METHOD_NOT_ALLOWED", "Method not allowed"),
    UNSUPPORTED_MEDIA_TYPE(HttpStatus.UNSUPPORTED_MEDIA_TYPE, "UNSUPPORTED_MEDIA_TYPE", "Unsupported media type"),
    DATA_INTEGRITY_VIOLATION(HttpStatus.CONFLICT, "DATA_INTEGRITY_VIOLATION", "Data integrity violation"),
    AUTH_REQUIRED(HttpStatus.UNAUTHORIZED, "AUTH_REQUIRED", "Authentication required"),

    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "VALIDATION_FAILED", "Validation failed"),
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "Bad request"),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "Unauthorized"),
    FORBIDDEN(HttpStatus.FORBIDDEN, "FORBIDDEN", "Forbidden"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND", "Resource not found"),
    CONFLICT(HttpStatus.CONFLICT, "CONFLICT", "Conflict"),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_SERVER_ERROR", "Internal server error");

    private final HttpStatus httpStatus;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String code, String message) {
        this.httpStatus = httpStatus;
        this.code = code;
        this.message = message;
    }

    public HttpStatus status() { return httpStatus; }      // GlobalExceptionHandler가 쓰는 메서드
    public String code() { return code; }                  // 혹시 쓰는 곳 있으면 대비
    public String message() { return message; }            // 혹시 쓰는 곳 있으면 대비

    public HttpStatus getHttpStatus() { return httpStatus; } // 다른 스타일 대비
    public String getCode() { return code; }                 // 다른 스타일 대비
    public String getMessage() { return message; }           // BusinessException이 필요
}

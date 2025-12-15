package com.minsu.webservice.global.error;

import java.time.Instant;

public record ApiResponse<T>(
        boolean isSuccess,
        String message,
        String code,
        String timestamp,
        T data
) {
    public static <T> ApiResponse<T> ok(String code, String message, T data) {
        return new ApiResponse<>(true, message, code, Instant.now().toString(), data);
    }

    public static ApiResponse<Void> ok(String code, String message) {
        return new ApiResponse<>(true, message, code, Instant.now().toString(), null);
    }

    // Added created methods
    public static <T> ApiResponse<T> created(String code, String message, T data) {
        return new ApiResponse<>(true, message, code, Instant.now().toString(), data);
    }

    public static ApiResponse<Void> created(String code, String message) {
        return new ApiResponse<>(true, message, code, Instant.now().toString(), null);
    }

    // Added noContent methods
    public static ApiResponse<Void> noContent(String code, String message) {
        return new ApiResponse<>(true, message, code, Instant.now().toString(), null);
    }
    public static <T> ApiResponse<T> noContent(String code, String message, T data) {
        return new ApiResponse<>(true, message, code, Instant.now().toString(), data);
    }
}

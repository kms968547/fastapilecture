package com.minsu.webservice.global.error;

import java.util.Map;

public record ErrorResponse(
        boolean isSuccess,
        String timestamp,
        String path,
        int status,
        String code,
        String message,
        Map<String, Object> details
) {
}
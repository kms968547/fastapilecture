package com.minsu.webservice.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minsu.webservice.global.error.ErrorCode;
import com.minsu.webservice.global.error.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import java.io.IOException;
import java.time.Instant;

public class JsonAccessDeniedHandler implements AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    public JsonAccessDeniedHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        ErrorCode ec = ErrorCode.FORBIDDEN;
        ErrorResponse body = new ErrorResponse(
                false,
                Instant.now().toString(),
                request.getRequestURI(),
                HttpServletResponse.SC_FORBIDDEN,
                ec.code(),
                ec.message(),
                null
        );

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), body);
    }
}

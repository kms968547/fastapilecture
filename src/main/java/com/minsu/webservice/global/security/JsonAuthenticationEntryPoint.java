package com.minsu.webservice.global.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minsu.webservice.global.error.ErrorCode;
import com.minsu.webservice.global.error.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;
import java.time.Instant;

public class JsonAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    public JsonAuthenticationEntryPoint(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        ErrorCode ec = ErrorCode.AUTH_REQUIRED;
        String code = ec.code();
        String message = ec.message();

        if (authException instanceof JwtAuthenticationException je) {
            code = je.getCode();
            message = je.getMessage();
        }

        ErrorResponse body = new ErrorResponse(
                false,
                Instant.now().toString(),
                request.getRequestURI(),
                HttpServletResponse.SC_UNAUTHORIZED,
                code,
                message,
                null
        );

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        objectMapper.writeValue(response.getWriter(), body);
    }
}

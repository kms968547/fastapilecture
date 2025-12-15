package com.minsu.webservice.global;

import com.minsu.webservice.global.error.ErrorCode;
import com.minsu.webservice.global.error.ErrorResponse;
import com.minsu.webservice.global.exception.ApiException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.dao.DataIntegrityViolationException;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse> handleApi(ApiException e, HttpServletRequest req) {
        ErrorCode ec = e.getErrorCode();
        return ResponseEntity.status(ec.status())
                .body(new ErrorResponse(
                        false,
                        Instant.now().toString(),
                        req.getRequestURI(),
                        ec.status().value(),
                        ec.code(),
                        ec.message(),
                        e.getDetails()
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidation(MethodArgumentNotValidException e, HttpServletRequest req) {
        Map<String, String> fields = new HashMap<>();
        for (FieldError fe : e.getBindingResult().getFieldErrors()) {
            fields.put(fe.getField(), fe.getDefaultMessage());
        }

        ErrorCode ec = ErrorCode.VALIDATION_FAILED;
        return ResponseEntity.status(ec.status())
                .body(new ErrorResponse(
                        false,
                        Instant.now().toString(),
                        req.getRequestURI(),
                        ec.status().value(),
                        ec.code(),
                        ec.message(),
                        Map.of("fields", fields)
                ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponse> handleBadJson(HttpMessageNotReadableException e, HttpServletRequest req) {
        ErrorCode ec = ErrorCode.MALFORMED_JSON;
        return ResponseEntity.status(ec.status())
                .body(new ErrorResponse(
                        false,
                        Instant.now().toString(),
                        req.getRequestURI(),
                        ec.status().value(),
                        ec.code(),
                        ec.message(),
                        Map.of("reason", "body_parse_failed")
                ));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMethodNotAllowed(HttpRequestMethodNotSupportedException e, HttpServletRequest req) {
        ErrorCode ec = ErrorCode.METHOD_NOT_ALLOWED;
        return ResponseEntity.status(ec.status())
                .body(new ErrorResponse(
                        false,
                        Instant.now().toString(),
                        req.getRequestURI(),
                        ec.status().value(),
                        ec.code(),
                        ec.message(),
                        Map.of("method", e.getMethod())
                ));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErrorResponse> handleMediaType(HttpMediaTypeNotSupportedException e, HttpServletRequest req) {
        ErrorCode ec = ErrorCode.UNSUPPORTED_MEDIA_TYPE;
        return ResponseEntity.status(ec.status())
                .body(new ErrorResponse(
                        false,
                        Instant.now().toString(),
                        req.getRequestURI(),
                        ec.status().value(),
                        ec.code(),
                        ec.message(),
                        Map.of("contentType", String.valueOf(e.getContentType()))
                ));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> handleForbidden(AccessDeniedException e, HttpServletRequest req) {
        ErrorCode ec = ErrorCode.FORBIDDEN;
        return ResponseEntity.status(ec.status())
                .body(new ErrorResponse(
                        false,
                        Instant.now().toString(),
                        req.getRequestURI(),
                        ec.status().value(),
                        ec.code(),
                        ec.message(),
                        null
                ));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErrorResponse> handleDataIntegrity(DataIntegrityViolationException e, HttpServletRequest req) {
        String msg = "";
        if (e.getMostSpecificCause() != null && e.getMostSpecificCause().getMessage() != null) {
            msg = e.getMostSpecificCause().getMessage();
        } else if (e.getMessage() != null) {
            msg = e.getMessage();
        }

        ErrorCode ec = guessIntegrityErrorCode(msg);
        return ResponseEntity.status(ec.status())
                .body(new ErrorResponse(
                        false,
                        Instant.now().toString(),
                        req.getRequestURI(),
                        ec.status().value(),
                        ec.code(),
                        ec.message(),
                        Map.of("db", "constraint_violation")
                ));
    }

    private ErrorCode guessIntegrityErrorCode(String msg) {
        String m = msg == null ? "" : msg.toLowerCase();
        if (m.contains("email") && (m.contains("duplicate") || m.contains("unique"))) {
            return ErrorCode.USER_DUPLICATE_EMAIL;
        }
        if (m.contains("duplicate") || m.contains("unique")) {
            return ErrorCode.DATA_INTEGRITY_VIOLATION;
        }
        return ErrorCode.DATA_INTEGRITY_VIOLATION;
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleUnknown(Exception e, HttpServletRequest req) {
        ErrorCode ec = ErrorCode.INTERNAL_SERVER_ERROR;
        return ResponseEntity.status(ec.status())
                .body(new ErrorResponse(
                        false,
                        Instant.now().toString(),
                        req.getRequestURI(),
                        ec.status().value(),
                        ec.code(),
                        ec.message(),
                        null
                ));
    }
}

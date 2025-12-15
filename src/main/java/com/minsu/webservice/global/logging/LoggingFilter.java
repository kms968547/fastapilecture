package com.minsu.webservice.global.logging;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

@Component
@Order(Ordered.HIGHEST_PRECEDENCE) // Ensure this filter runs first
public class LoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        Instant start = Instant.now();
        String method = request.getMethod();
        String path = request.getRequestURI();

        try {
            filterChain.doFilter(request, response);
        } finally {
            Instant end = Instant.now();
            long duration = Duration.between(start, end).toMillis();
            int status = response.getStatus();

            log.info("Request: {} {} -> Response: {} ({}ms)", method, path, status, duration);
        }
    }
}

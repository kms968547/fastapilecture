package com.minsu.webservice.domain.auth.dto;

public record LoginPayload(
        String accessToken,
        String refreshToken,
        long expiresIn,
        long user_id,
        String role,
        String token_type
) {
}

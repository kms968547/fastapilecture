package com.minsu.webservice.domain.auth.dto;

public record RefreshPayload(
        String accessToken,
        String refreshToken,
        long expiresIn,
        String token_type
) {
}

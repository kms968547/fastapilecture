package com.minsu.webservice.domain.user.dto;

import com.minsu.webservice.domain.user.User;

public record UserResponse(
    Long id,
    String email,
    String name,
    String role
) {
    public static UserResponse from(User user) {
        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole().name()
        );
    }
}

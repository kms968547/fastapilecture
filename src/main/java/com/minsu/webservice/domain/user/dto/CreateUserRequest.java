package com.minsu.webservice.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record CreateUserRequest(
        @NotBlank String email,
        @NotBlank String password,
        @NotBlank String name,
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}") String birthdate,
        @NotBlank String gender,
        @NotBlank String address
) {
}
package com.minsu.webservice.domain.user.dto;

import jakarta.validation.constraints.Pattern;

public record UpdateUserRequest(
        String name,
        @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}") String birthdate,
        String gender,
        String address,
        String phonenumber,
        String password
) {
}

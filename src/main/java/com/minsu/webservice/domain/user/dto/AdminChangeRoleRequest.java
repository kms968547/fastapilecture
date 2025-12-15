package com.minsu.webservice.domain.user.dto;

import jakarta.validation.constraints.NotBlank;

public record AdminChangeRoleRequest(
        @NotBlank String role
) {
}

package com.minsu.webservice.domain.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record CreateOrderRequest(
        @NotNull(message = "Order items cannot be null")
        @NotEmpty(message = "Order must contain at least one item")
        List<@Valid OrderItemDto> items
) {}

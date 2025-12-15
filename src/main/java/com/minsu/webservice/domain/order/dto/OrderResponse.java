package com.minsu.webservice.domain.order.dto;

import com.minsu.webservice.domain.order.Order;
import com.minsu.webservice.domain.order.OrderItem;
import com.minsu.webservice.domain.order.OrderStatus;
import com.minsu.webservice.domain.user.dto.UserResponse;
import com.minsu.webservice.domain.book.dto.BookResponse; // Assuming BookResponse exists
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record OrderResponse(
        Long id,
        UserResponse user,
        LocalDateTime orderDate,
        OrderStatus status,
        List<OrderItemResponse> items
) {
    public static OrderResponse from(Order order) {
        List<OrderItemResponse> itemResponses = order.getOrderItems().stream()
                .map(OrderItemResponse::from)
                .collect(Collectors.toList());

        return new OrderResponse(
                order.getId(),
                UserResponse.from(order.getUser()),
                order.getOrderDate(),
                order.getStatus(),
                itemResponses
        );
    }
}

record OrderItemResponse(
        Long id,
        // BookResponse book, // Removed to avoid circular dependency and simplify
        Long bookId,
        String bookTitle,
        Integer quantity,
        Double price
) {
    public static OrderItemResponse from(OrderItem item) {
        return new OrderItemResponse(
                item.getId(),
                item.getBook().getId(),
                item.getBook().getTitle(),
                item.getQuantity(),
                item.getPrice()
        );
    }
}

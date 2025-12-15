package com.minsu.webservice.domain.order;

public enum OrderStatus {
    PENDING,        // Order placed, awaiting processing
    PROCESSING,     // Order being prepared
    SHIPPED,        // Order has been shipped
    DELIVERED,      // Order has been delivered
    CANCELLED,      // Order was cancelled
    RETURNED        // Order was returned
}

package com.minsu.webservice.domain.order;

import org.springframework.data.jpa.domain.Specification;

public class OrderSpecification {

    public static Specification<Order> filterByUser(Long userId) {
        return (root, query, criteriaBuilder) -> {
            if (userId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("user").get("id"), userId);
        };
    }

    public static Specification<Order> filterByStatus(OrderStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (status == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("status"), status);
        };
    }
    
    // Potentially add more filtering options here (e.g., by order date range)
}

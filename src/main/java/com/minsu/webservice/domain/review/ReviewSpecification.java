package com.minsu.webservice.domain.review;

import jakarta.persistence.criteria.JoinType;
import org.springframework.data.jpa.domain.Specification;

public class ReviewSpecification {

    public static Specification<Review> filterByBook(Long bookId) {
        return (root, query, criteriaBuilder) -> {
            if (bookId == null) {
                return criteriaBuilder.conjunction();
            }
            // Join with Book entity and filter by bookId
            return criteriaBuilder.equal(root.join("book", JoinType.INNER).get("id"), bookId);
        };
    }
    
    // Potentially add more filtering options here (e.g., by user, by rating range)
}

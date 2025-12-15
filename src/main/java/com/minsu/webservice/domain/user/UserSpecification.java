package com.minsu.webservice.domain.user;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class UserSpecification {

    public static Specification<User> search(String keyword, UserRole role) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Keyword search (e.g., in username or email)
            if (StringUtils.hasText(keyword)) {
                String likeKeyword = "%" + keyword.toLowerCase() + "%";
                Predicate nameLike = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), likeKeyword); // Changed from "username" to "name"
                Predicate emailLike = criteriaBuilder.like(criteriaBuilder.lower(root.get("email")), likeKeyword);
                predicates.add(criteriaBuilder.or(nameLike, emailLike));
            }

            // Role filter
            if (role != null) {
                predicates.add(criteriaBuilder.equal(root.get("role"), role));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

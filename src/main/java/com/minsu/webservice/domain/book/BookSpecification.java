package com.minsu.webservice.domain.book;

import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class BookSpecification {

    public static Specification<Book> search(String keyword, String genre, Integer publicationYear) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            // Keyword search (e.g., in title or author)
            if (StringUtils.hasText(keyword)) {
                String likeKeyword = "%" + keyword.toLowerCase() + "%";
                Predicate titleLike = criteriaBuilder.like(criteriaBuilder.lower(root.get("title")), likeKeyword);
                Predicate authorLike = criteriaBuilder.like(criteriaBuilder.lower(root.get("author")), likeKeyword);
                predicates.add(criteriaBuilder.or(titleLike, authorLike));
            }

            // Genre filter
            if (StringUtils.hasText(genre)) {
                predicates.add(criteriaBuilder.equal(criteriaBuilder.lower(root.get("genre")), genre.toLowerCase()));
            }

            // Publication Year filter
            if (publicationYear != null) {
                predicates.add(criteriaBuilder.equal(root.get("publicationYear"), publicationYear));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}

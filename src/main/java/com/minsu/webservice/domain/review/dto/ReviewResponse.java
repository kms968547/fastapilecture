package com.minsu.webservice.domain.review.dto;

import com.minsu.webservice.domain.review.Review;
import com.minsu.webservice.domain.user.dto.UserResponse; // Assuming UserResponse exists
import com.minsu.webservice.domain.book.dto.BookResponse; // Assuming BookResponse exists
import java.time.LocalDateTime;

public record ReviewResponse(
        Long id,
        UserResponse user,
        BookResponse book,
        String comment,
        Integer rating,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ReviewResponse from(Review review) {
        // Note: This 'from' method needs to be careful about N+1 issues
        // It should either receive pre-fetched User and Book objects
        // or defer their fetching to the service layer when creating UserResponse/BookResponse.
        // For simplicity in DTO, assuming UserResponse.from and BookResponse.from can handle it.
        return new ReviewResponse(
                review.getId(),
                UserResponse.from(review.getUser()),
                BookResponse.from(review.getBook()),
                review.getComment(),
                review.getRating(),
                review.getCreatedAt(),
                review.getUpdatedAt()
        );
    }
}

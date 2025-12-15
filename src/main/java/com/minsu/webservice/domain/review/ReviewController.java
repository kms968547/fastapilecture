package com.minsu.webservice.domain.review;

import com.minsu.webservice.domain.review.dto.CreateReviewRequest;
import com.minsu.webservice.domain.review.dto.ReviewResponse;
import com.minsu.webservice.global.dto.PageRequest;
import com.minsu.webservice.global.dto.SortCriteria;
import com.minsu.webservice.global.error.ApiResponse;
import com.minsu.webservice.global.security.UserPrincipal;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping("/reviews")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<ReviewResponse> createReview(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody @Valid CreateReviewRequest request
    ) {
        ReviewResponse createdReview = reviewService.createReview(userPrincipal.userId(), request);
        return ApiResponse.created("REVIEW_CREATED", "Review created successfully", createdReview);
    }

    @GetMapping("/reviews/{id}")
    public ApiResponse<ReviewResponse> getReview(@PathVariable Long id) {
        ReviewResponse review = reviewService.getReview(id);
        return ApiResponse.ok("REVIEW_FOUND", "Review found successfully", review);
    }

    @GetMapping("/books/{bookId}/reviews")
    public ApiResponse<Page<ReviewResponse>> listReviewsForBook(
            @PathVariable Long bookId,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "createdAt,DESC") String sort
    ) {
        PageRequest pageRequest = new PageRequest(page, size);
        SortCriteria sortCriteria = new SortCriteria(sort);
        Page<ReviewResponse> reviews = reviewService.listReviews(bookId, pageRequest, sortCriteria);
        return ApiResponse.ok("BOOK_REVIEWS_LISTED", "Book reviews listed successfully", reviews);
    }

    @PutMapping("/reviews/{id}")
    public ApiResponse<ReviewResponse> updateReview(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestBody @Valid CreateReviewRequest request // Reusing CreateReviewRequest for update for simplicity
    ) {
        ReviewResponse updatedReview = reviewService.updateReview(id, userPrincipal.userId(), request.comment(), request.rating());
        return ApiResponse.ok("REVIEW_UPDATED", "Review updated successfully", updatedReview);
    }

    @DeleteMapping("/reviews/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Void> deleteReview(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        reviewService.deleteReview(id, userPrincipal.userId());
        return ApiResponse.noContent("REVIEW_DELETED", "Review deleted successfully");
    }

    // Admin-only endpoint
    @DeleteMapping("/admin/reviews/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Void> adminDeleteReview(@PathVariable Long id) {
        reviewService.adminDeleteReview(id);
        return ApiResponse.noContent("ADMIN_REVIEW_DELETED", "Admin deleted review successfully");
    }
}

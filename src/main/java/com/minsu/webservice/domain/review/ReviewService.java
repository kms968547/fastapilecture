package com.minsu.webservice.domain.review;

import com.minsu.webservice.domain.book.Book;
import com.minsu.webservice.domain.book.BookRepository;
import com.minsu.webservice.domain.review.dto.CreateReviewRequest;
import com.minsu.webservice.domain.review.dto.ReviewResponse;
import com.minsu.webservice.domain.user.User;
import com.minsu.webservice.domain.user.UserRepository;
import com.minsu.webservice.global.dto.PageRequest;
import com.minsu.webservice.global.dto.SortCriteria;
import com.minsu.webservice.global.exception.ApiException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.minsu.webservice.global.error.ErrorCode.*;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;

    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository, BookRepository bookRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.bookRepository = bookRepository;
    }

    @Transactional
    public ReviewResponse createReview(Long userId, CreateReviewRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ApiException(USER_NOT_FOUND));
        Book book = bookRepository.findById(request.bookId())
                .orElseThrow(() -> new ApiException(NOT_FOUND)); // Use a more specific error for Book not found

        Review review = new Review(user, book, request.comment(), request.rating());
        return ReviewResponse.from(reviewRepository.save(review));
    }

    @Transactional(readOnly = true)
    public ReviewResponse getReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ApiException(NOT_FOUND)); // Use a more specific error for Review not found
        return ReviewResponse.from(review);
    }

    @Transactional(readOnly = true)
    public Page<ReviewResponse> listReviews(Long bookId, PageRequest pageRequest, SortCriteria sortCriteria) {
        Specification<Review> spec = ReviewSpecification.filterByBook(bookId);
        Pageable pageable = pageRequest.toPageable(sortCriteria.toSort());
        return reviewRepository.findAll(spec, pageable).map(ReviewResponse::from);
    }

    @Transactional
    public ReviewResponse updateReview(Long reviewId, Long userId, String comment, Integer rating) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ApiException(NOT_FOUND));

        if (!review.getUser().getId().equals(userId)) {
            throw new ApiException(FORBIDDEN); // User can only update their own reviews
        }
        
        if (comment != null) review.setComment(comment);
        if (rating != null) review.setRating(rating);
        
        return ReviewResponse.from(reviewRepository.save(review));
    }

    @Transactional
    public void deleteReview(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ApiException(NOT_FOUND));

        if (!review.getUser().getId().equals(userId)) {
            throw new ApiException(FORBIDDEN); // User can only delete their own reviews
        }
        reviewRepository.delete(review);
    }

    @Transactional
    public void adminDeleteReview(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ApiException(NOT_FOUND));
        reviewRepository.delete(review);
    }
}

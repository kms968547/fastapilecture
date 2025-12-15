package com.minsu.webservice.domain.book.dto;

import com.minsu.webservice.domain.book.Book;

public record BookResponse(
    Long id,
    String title,
    String author,
    String isbn,
    Integer publicationYear,
    String genre,
    Integer availableCopies,
    Integer totalCopies,
    Long sellerId, // Already added
    String publisher, // Already added
    String summary, // Already added
    Double price // Added
) {
    public static BookResponse from(Book book) {
        return new BookResponse(
            book.getId(),
            book.getTitle(),
            book.getAuthor(),
            book.getIsbn(),
            book.getPublicationYear(),
            book.getGenre(),
            book.getAvailableCopies(),
            book.getTotalCopies(),
            book.getSeller().getId(),
            book.getPublisher(),
            book.getSummary(),
            book.getPrice() // Added
        );
    }
}

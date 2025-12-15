package com.minsu.webservice.domain.book.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

public record UpdateBookRequest(
        @Size(min = 1, max = 255, message = "Title must be between 1 and 255 characters")
        String title,

        @Size(min = 1, max = 255, message = "Author must be between 1 and 255 characters")
        String author,

        Integer publicationYear,

        @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "Publication date must be in YYYY-MM-DD format")
        String publicationDate,

        @Size(max = 100, message = "Genre cannot exceed 100 characters")
        String genre,

        @Min(value = 0, message = "Total copies cannot be negative")
        Integer totalCopies,

        @Size(min = 1, max = 255, message = "Publisher must be between 1 and 255 characters")
        String publisher,

        @Size(max = 1000, message = "Summary cannot exceed 1000 characters")
        String summary,

        @Min(value = 0, message = "Price cannot be negative")
        Double price
) {}

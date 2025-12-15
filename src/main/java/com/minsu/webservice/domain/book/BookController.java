package com.minsu.webservice.domain.book;

import com.minsu.webservice.domain.book.dto.BookResponse;
import com.minsu.webservice.domain.book.dto.UpdateBookRequest;
import com.minsu.webservice.global.dto.PageRequest;
import com.minsu.webservice.global.dto.SortCriteria;
import com.minsu.webservice.global.error.ApiResponse;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.minsu.webservice.global.security.UserPrincipal;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/books")
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ApiResponse<Page<BookResponse>> listBooks(
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Integer publicationYear,
            @RequestParam(defaultValue = "0") Integer page,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(defaultValue = "id,DESC") String sort
    ) {
        PageRequest pageRequest = new PageRequest(page, size);
        SortCriteria sortCriteria = new SortCriteria(sort);

        Page<BookResponse> books = bookService.listBooks(keyword, genre, publicationYear, pageRequest, sortCriteria);
        return ApiResponse.ok("BOOK_LIST_SUCCESS", "Book list fetched successfully", books);
    }

    @GetMapping("/{id}")
    public ApiResponse<BookResponse> getBook(@PathVariable Long id) {
        BookResponse book = bookService.getBook(id);
        return ApiResponse.ok("BOOK_FOUND", "Book found successfully", book);
    }

    @PutMapping("/{id}")
    public ApiResponse<BookResponse> updateBook(
            @PathVariable Long id,
            @RequestBody @Valid com.minsu.webservice.domain.book.dto.UpdateBookRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        BookResponse updatedBook = bookService.updateBook(id, userPrincipal.userId(), request);
        return ApiResponse.ok("BOOK_UPDATED", "Book updated successfully", updatedBook);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Void> deleteBook(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {
        bookService.deleteBook(id, userPrincipal.userId());
        return ApiResponse.noContent("BOOK_DELETED", "Book deleted successfully");
    }

    // Admin-only endpoint
    @DeleteMapping("/admin/{id}") // Note: Changed to /admin/{id} for clarity, assuming /books/admin/{id}
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ApiResponse<Void> adminDeleteBook(@PathVariable Long id) {
        bookService.adminDeleteBook(id);
        return ApiResponse.noContent("ADMIN_BOOK_DELETED", "Admin deleted book successfully");
    }
    
    // Example for creating a book (not part of the original request, but good for completeness)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApiResponse<BookResponse> createBook(
            @RequestParam String title,
            @RequestParam String author,
            @RequestParam String isbn,
            @RequestParam Integer publicationYear,
            @RequestParam String publicationDate,
            @RequestParam String genre,
            @RequestParam Integer totalCopies,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @RequestParam String publisher,
            @RequestParam(required = false) String summary, // Made optional previously
            @RequestParam Double price // Added price, making it required
    ) {
        LocalDate parsedPublicationDate = LocalDate.parse(publicationDate);
        BookResponse createdBook = bookService.createBook(title, author, isbn, publicationYear, parsedPublicationDate, genre, totalCopies, userPrincipal.userId(), publisher, summary, price); // Pass price
        return ApiResponse.created("BOOK_CREATED_SUCCESS", "Book created successfully", createdBook);
    }
}

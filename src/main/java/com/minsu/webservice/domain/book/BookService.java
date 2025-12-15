package com.minsu.webservice.domain.book;

import com.minsu.webservice.domain.book.dto.BookResponse;
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
import java.time.LocalDate;
import java.util.Optional; // Import Optional

import static com.minsu.webservice.global.error.ErrorCode.USER_NOT_FOUND;
import static com.minsu.webservice.global.error.ErrorCode.BOOK_NOT_FOUND;
import static com.minsu.webservice.global.error.ErrorCode.FORBIDDEN;

@Service
public class BookService {

    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public BookService(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    // This method will handle listing books with pagination, sorting, and filtering
    public Page<BookResponse> listBooks(String keyword, String genre, Integer publicationYear, PageRequest pageRequest, SortCriteria sortCriteria) {
        Specification<Book> spec = BookSpecification.search(keyword, genre, publicationYear);

        Pageable pageable = pageRequest.toPageable(sortCriteria.toSort());

        Page<Book> page = bookRepository.findAll(spec, pageable);

        return page.map(BookResponse::from);
    }

    @Transactional(readOnly = true)
    public BookResponse getBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ApiException(BOOK_NOT_FOUND));
        return BookResponse.from(book);
    }

    @Transactional
    public BookResponse updateBook(Long bookId, Long userId, com.minsu.webservice.domain.book.dto.UpdateBookRequest request) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ApiException(BOOK_NOT_FOUND));

        if (!book.getSeller().getId().equals(userId)) {
            throw new ApiException(FORBIDDEN); // Only the seller can update their book
        }

        LocalDate parsedPublicationDate = null;
        if (request.publicationDate() != null) {
            parsedPublicationDate = LocalDate.parse(request.publicationDate());
        }

        book.update(
            request.title(),
            request.author(),
            request.publicationYear(),
            parsedPublicationDate,
            request.genre(),
            request.totalCopies(),
            request.publisher(),
            request.summary(),
            request.price()
        );
        return BookResponse.from(bookRepository.save(book));
    }

    @Transactional
    public void deleteBook(Long bookId, Long userId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ApiException(BOOK_NOT_FOUND));

        if (!book.getSeller().getId().equals(userId)) {
            throw new ApiException(FORBIDDEN); // Only the seller can delete their book
        }
        bookRepository.delete(book);
    }

    @Transactional
    public void adminDeleteBook(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ApiException(BOOK_NOT_FOUND));
        bookRepository.delete(book);
    }

    // Example method for creating a book (optional, but good for completeness)
    @Transactional
    public BookResponse createBook(String title, String author, String isbn, Integer publicationYear, LocalDate publicationDate, String genre, Integer totalCopies, Long sellerId, String publisher, String summary, Double price) {
        User seller = userRepository.findById(sellerId)
                .orElseThrow(() -> new ApiException(USER_NOT_FOUND));

        Optional<Book> existingBook = bookRepository.findByIsbn(isbn);
        if (existingBook.isPresent()) {
            throw new IllegalArgumentException("Book with ISBN " + isbn + " already exists.");
        }
        Book newBook = new Book(title, author, isbn, publicationYear, publicationDate, genre, totalCopies, seller, publisher, summary, price);
        Book savedBook = bookRepository.save(newBook);
        return BookResponse.from(savedBook);
    }
}

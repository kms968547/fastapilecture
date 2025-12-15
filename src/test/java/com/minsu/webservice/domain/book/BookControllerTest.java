package com.minsu.webservice.domain.book;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minsu.webservice.WebserviceApplication;
import com.minsu.webservice.domain.review.ReviewRepository; // Added
import com.minsu.webservice.domain.order.OrderRepository; // Added
import com.minsu.webservice.domain.order.OrderItemRepository; // Added
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.time.LocalDate;
import java.util.stream.IntStream;
import com.minsu.webservice.domain.user.User;
import com.minsu.webservice.domain.user.UserRepository;
import com.minsu.webservice.domain.user.UserGender;
import com.minsu.webservice.domain.user.UserRole;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;

@SpringBootTest(classes = WebserviceApplication.class)
@ActiveProfiles("test")
class BookControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReviewRepository reviewRepository; // Injected
    @Autowired
    private OrderRepository orderRepository; // Injected
    @Autowired
    private OrderItemRepository orderItemRepository; // Injected

    private User testUser;

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity()) // Apply Spring Security filters, though not strictly needed for /books without auth
                .build();
        reviewRepository.deleteAll(); // Clear existing reviews first
        orderItemRepository.deleteAll(); // Clear existing order items
        orderRepository.deleteAll(); // Clear existing orders
        bookRepository.deleteAll(); // Clear existing books
        userRepository.deleteAll(); // Clear existing users

        this.testUser = userRepository.save(new User("test@example.com", "password", "Test User", LocalDate.of(1990, 1, 1), UserGender.MALE, UserRole.ROLE_USER, "1234567890", "Test Address"));

        // Create some test books
        IntStream.range(0, 5).forEach(i -> {
            Book book = new Book("Title " + i, "Author " + i, "978-0-123456-0" + i,
                    2000 + i, LocalDate.of(2000 + i, 1, 1), "Genre A", 10 + i, testUser, "Publisher X", "Summary for book " + i, 10.00 + i);
            bookRepository.save(book);
        });
        IntStream.range(5, 10).forEach(i -> {
            Book book = new Book("Another Title " + i, "Another Author " + i, "978-0-123456-1" + i,
                    2010 + i, LocalDate.of(2010 + i, 1, 1), "Genre B", 20 + i, testUser, "Publisher Y", "Summary for book " + i, 20.00 + i); // Added sellerId
            bookRepository.save(book);
        });
        bookRepository.save(new Book("Specific Book Title", "Specific Author", "978-1-111111-123", 2020, LocalDate.of(2020, 1, 1), "Fiction", 5, testUser, "Publisher Z", "A very specific book summary.", 15.75)); // Added sellerId
        // Total 11 books
    }

    @Test
    @DisplayName("도서 목록을 페이지네이션하여 조회한다")
    void listBooks_pagination() throws Exception {
        mockMvc.perform(get("/books")
                        .param("page", "0")
                        .param("size", "5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.data.content.length()").value(5))
                .andExpect(jsonPath("$.data.totalElements").value(11))
                .andExpect(jsonPath("$.data.totalPages").value(3))
                .andExpect(jsonPath("$.data.number").value(0))
                .andExpect(jsonPath("$.data.size").value(5));
    }

    @Test
    @DisplayName("도서 목록을 제목 기준으로 오름차순 정렬하여 조회한다")
    void listBooks_sortByTitleAsc() throws Exception {
        mockMvc.perform(get("/books")
                        .param("page", "0")
                        .param("size", "11")
                        .param("sort", "title,ASC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.data.content.length()").value(11))
                .andExpect(jsonPath("$.data.content[0].title").value("Another Title 5")) // "Another Title 5", "Another Title 6", ... "Specific Book Title", "Title 0", ...
                .andExpect(jsonPath("$.data.content[1].title").value("Another Title 6"));
    }

    @Test
    @DisplayName("도서 목록을 저자 기준으로 내림차순 정렬하여 조회한다")
    void listBooks_sortByAuthorDesc() throws Exception {
        mockMvc.perform(get("/books")
                        .param("page", "0")
                        .param("size", "11")
                        .param("sort", "author,DESC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.data.content.length()").value(11))
                .andExpect(jsonPath("$.data.content[0].author").value("Specific Author")); // Specific Author, then Title 9, Title 8, ...
    }

    @Test
    @DisplayName("도서 목록을 키워드로 필터링하여 조회한다 (제목)")
    void listBooks_filterByKeywordTitle() throws Exception {
        mockMvc.perform(get("/books")
                        .param("page", "0")
                        .param("size", "10")
                        .param("keyword", "Specific Book"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.data.content.length()").value(1))
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andExpect(jsonPath("$.data.content[0].title").value("Specific Book Title"));
    }

    @Test
    @DisplayName("도서 목록을 장르로 필터링하여 조회한다")
    void listBooks_filterByGenre() throws Exception {
        mockMvc.perform(get("/books")
                        .param("page", "0")
                        .param("size", "10")
                        .param("genre", "Genre A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.data.content.length()").value(5))
                .andExpect(jsonPath("$.data.totalElements").value(5));
    }

    @Test
    @DisplayName("도서 목록을 출판 연도로 필터링하여 조회한다")
    void listBooks_filterByPublicationYear() throws Exception {
        mockMvc.perform(get("/books")
                        .param("page", "0")
                        .param("size", "10")
                        .param("publicationYear", "2000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.data.content.length()").value(1))
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andExpect(jsonPath("$.data.content[0].title").value("Title 0"));
    }

    @Test
    @DisplayName("도서 목록을 키워드와 장르로 필터링하여 조회한다")
    void listBooks_filterByKeywordAndGenre() throws Exception {
        mockMvc.perform(get("/books")
                        .param("page", "0")
                        .param("size", "10")
                        .param("keyword", "Author 1")
                        .param("genre", "Genre A"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.data.content.length()").value(1))
                .andExpect(jsonPath("$.data.totalElements").value(1))
                .andExpect(jsonPath("$.data.content[0].author").value("Author 1"));
    }
    
    @Test
    @DisplayName("페이지 크기가 최대 크기를 초과하면 기본 최대 크기가 적용된다")
    void listBooks_sizeExceedsMaxSize() throws Exception {
        mockMvc.perform(get("/books")
                        .param("page", "0")
                        .param("size", "200")) // MAX_SIZE is 100
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.data.size").value(100)); // Should be capped at 100
    }

    @Test
    @DisplayName("파라미터 미지정 시 기본 페이지네이션 및 정렬이 적용된다")
    void listBooks_defaultParams() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.data.content.length()").value(10)) // Default size is 10
                .andExpect(jsonPath("$.data.totalElements").value(11))
                .andExpect(jsonPath("$.data.number").value(0)) // Default page is 0
                .andExpect(jsonPath("$.data.size").value(10));
    }
}

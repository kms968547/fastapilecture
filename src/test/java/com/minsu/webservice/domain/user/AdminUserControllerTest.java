package com.minsu.webservice.domain.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.minsu.webservice.WebserviceApplication;
import com.minsu.webservice.domain.book.BookRepository;
import com.minsu.webservice.domain.review.ReviewRepository;
import com.minsu.webservice.domain.order.OrderItemRepository; // Added
import com.minsu.webservice.domain.order.OrderRepository; // Added
import com.minsu.webservice.global.security.JwtProvider;
import com.minsu.webservice.global.security.UserPrincipal; // Added
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders; // Added
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.stream.IntStream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import org.springframework.web.context.WebApplicationContext; // Added
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity; // Left this here as it's still needed to apply to MockMvcBuilders

@SpringBootTest(classes = WebserviceApplication.class)
@ActiveProfiles("test")
class AdminUserControllerTest {

    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext context; // Added

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository; // Injected
    
    @Autowired
    private ReviewRepository reviewRepository; 

    @Autowired
    private OrderRepository orderRepository; // Injected
    
    @Autowired
    private OrderItemRepository orderItemRepository; // Injected

    @Autowired
    private JwtProvider jwtProvider; // Still needed for token generation for some other potential tests, but not for direct auth in MockMvc. Let's keep it for now.

    private UserPrincipal adminUserPrincipal; // Changed to UserPrincipal
    // private String adminToken; // Removed

    @BeforeEach
    void setUp() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity())
                .build();
        // Clear existing data in correct order to avoid foreign key constraint violations
        reviewRepository.deleteAll();
        orderItemRepository.deleteAll(); // Clear OrderItem first
        orderRepository.deleteAll(); // Clear Order next
        bookRepository.deleteAll();
        userRepository.deleteAll();

        // Create an admin user
        User adminUser = new User("admin@example.com", "password", "Admin User",
                LocalDate.of(1990, 1, 1), UserGender.MALE, UserRole.ROLE_ADMIN,
                "01012345678", "Admin Address");
        userRepository.save(adminUser);
        adminUserPrincipal = UserPrincipal.from(adminUser); // Create UserPrincipal
        // adminToken = jwtProvider.createAccessToken(adminUser.getId(), adminUser.getEmail(), adminUser.getRole().name()); // Not needed for .with(user())

        // Create some test users
        IntStream.range(0, 5).forEach(i -> {
            User user = new User("user" + i + "@example.com", "password", "User " + i,
                    LocalDate.of(2000, 1, 1), UserGender.FEMALE, UserRole.ROLE_USER,
                    "0102222333" + i, "User Address " + i);
            userRepository.save(user);
        });
        IntStream.range(5, 10).forEach(i -> {
            User user = new User("user" + i + "@example.com", "password", "AdminUser " + i,
                    LocalDate.of(1995, 1, 1), UserGender.MALE, UserRole.ROLE_ADMIN,
                    "0101111222" + i, "Admin User Address " + i);
            userRepository.save(user);
        });
        // Total 11 users (1 admin, 5 user-roles, 5 admin-roles)
    }

    @Test
    @DisplayName("관리자 권한으로 사용자 목록을 페이지네이션하여 조회한다")
    void listUsers_pagination() throws Exception {
        mockMvc.perform(get("/admin/users")
                        .with(user(adminUserPrincipal))                        .param("page", "0")
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
    @DisplayName("관리자 권한으로 사용자 목록을 이름 기준으로 오름차순 정렬하여 조회한다")
    void listUsers_sortByNameAsc() throws Exception {
        mockMvc.perform(get("/admin/users")
                        .with(user(adminUserPrincipal))                        .param("page", "0")
                        .param("size", "11") // Get all to check sort
                        .param("sort", "name,ASC"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.data.content.length()").value(11))
                // Note: The actual sorted order depends on how names are generated and sorted by MySQL.
                // Assuming "Admin User" comes before "AdminUser 5" due to space.
                .andExpect(jsonPath("$.data.content[0].name").value("Admin User"))
                .andExpect(jsonPath("$.data.content[1].name").value("AdminUser 5")); // This might need adjustment based on actual sort
    }

    @Test
    @DisplayName("관리자 권한으로 사용자 목록을 역할 필터링하여 조회한다")
    void listUsers_filterByRole() throws Exception {
        mockMvc.perform(get("/admin/users")
                        .with(user(adminUserPrincipal))                        .param("page", "0")
                        .param("size", "10")
                        .param("role", "ROLE_USER"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.data.content.length()").value(5))
                .andExpect(jsonPath("$.data.totalElements").value(5))
                .andExpect(jsonPath("$.data.content[0].role").value("ROLE_USER"));
    }

    @Test
    @DisplayName("관리자 권한으로 사용자 목록을 키워드로 필터링하여 조회한다 (이름)")
    void listUsers_filterByKeywordName() throws Exception {
                mockMvc.perform(get("/admin/users")
                                .with(user(adminUserPrincipal))
                                .param("page", "0")
                                .param("size", "10")
                                .param("keyword", "AdminUser") // "AdminUser 5" to "AdminUser 9"
                                .param("sort", "name,ASC")) // Added explicit sort
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.isSuccess").value(true))
                        .andExpect(jsonPath("$.data.content.length()").value(5))
                        .andExpect(jsonPath("$.data.totalElements").value(5))
                        .andExpect(jsonPath("$.data.content[0].name").value("AdminUser 5"));
    }

    @Test
    @DisplayName("관리자 권한으로 사용자 목록을 키워드로 필터링하여 조회한다 (이메일)")
    void listUsers_filterByKeywordEmail() throws Exception {
                mockMvc.perform(get("/admin/users")
                                .with(user(adminUserPrincipal))
                                .param("page", "0")
                                .param("size", "10")
                                .param("keyword", "user0@example.com") // Only user0
                                .param("sort", "name,ASC")) // Added explicit sort
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.isSuccess").value(true))
                        .andExpect(jsonPath("$.data.content.length()").value(1))
                        .andExpect(jsonPath("$.data.totalElements").value(1))
                        .andExpect(jsonPath("$.data.content[0].email").value("user0@example.com"));
    }

    @Test
    @DisplayName("관리자 권한으로 사용자 목록을 키워드와 역할로 필터링하여 조회한다")
    void listUsers_filterByKeywordAndRole() throws Exception {
                mockMvc.perform(get("/admin/users")
                                .with(user(adminUserPrincipal))
                                .param("page", "0")
                                .param("size", "10")
                                .param("keyword", "User")
                                .param("role", "ROLE_USER")
                                .param("sort", "name,ASC")) // Added explicit sort
                        .andExpect(status().isOk())
                        .andExpect(jsonPath("$.isSuccess").value(true))
                        .andExpect(jsonPath("$.data.content.length()").value(5))
                        .andExpect(jsonPath("$.data.totalElements").value(5))
                        .andExpect(jsonPath("$.data.content[0].name").value("User 0"));
    }

    @Test
    @DisplayName("관리자 권한 없이 사용자 목록 조회 시 접근이 거부된다")
    void listUsers_unauthorized() throws Exception {
        User regularUser = new User("regular@example.com", "password", "Regular User",
                LocalDate.of(1990, 1, 1), UserGender.FEMALE, UserRole.ROLE_USER,
                "01098765432", "Regular Address");
        userRepository.save(regularUser);
        UserPrincipal regularUserPrincipal = UserPrincipal.from(regularUser); // Create UserPrincipal for regular user

        mockMvc.perform(get("/admin/users")
                        .with(user(regularUserPrincipal))) // Authenticate as a regular user
                .andExpect(status().isForbidden());
    }

    // Edge case for size exceeding MAX_SIZE
    @Test
    @DisplayName("페이지 크기가 최대 크기를 초과하면 기본 최대 크기가 적용된다")
    void listUsers_sizeExceedsMaxSize() throws Exception {
        mockMvc.perform(get("/admin/users")
                        .with(user(adminUserPrincipal))                        .param("page", "0")
                        .param("size", "200")) // MAX_SIZE is 100
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.data.size").value(100)); // Should be capped at 100
    }

    // Default values test
    @Test
    @DisplayName("파라미터 미지정 시 기본 페이지네이션 및 정렬이 적용된다")
    void listUsers_defaultParams() throws Exception {
        mockMvc.perform(get("/admin/users")
                        .with(user(adminUserPrincipal))) // Use with(user(...))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.data.content.length()").value(10)) // Default size is 10
                .andExpect(jsonPath("$.data.totalElements").value(11))
                .andExpect(jsonPath("$.data.number").value(0)) // Default page is 0
                .andExpect(jsonPath("$.data.size").value(10));
    }
}
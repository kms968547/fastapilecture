package com.minsu.webservice.domain.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor; // Added
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    Page<User> findByNameContainingIgnoreCase(String keyword, Pageable pageable);
    Page<User> findByRole(UserRole role, Pageable pageable);
    Page<User> findByRoleAndNameContainingIgnoreCase(UserRole role, String keyword, Pageable pageable);

}

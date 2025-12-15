package com.minsu.webservice.domain.user;

import com.minsu.webservice.global.security.UserPrincipal;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification; // Added
import com.minsu.webservice.domain.user.dto.CreateUserRequest;
import com.minsu.webservice.domain.user.dto.UserResponse;
import com.minsu.webservice.global.dto.PageRequest; // Added
import com.minsu.webservice.global.dto.SortCriteria; // Added
import com.minsu.webservice.domain.user.UserSpecification; // Added
import com.minsu.webservice.global.error.ErrorCode;
import com.minsu.webservice.global.exception.ApiException;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.Map;
import java.util.Objects;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserResponse create(CreateUserRequest req) {
        if (userRepository.existsByEmail(req.email())) {
            throw new ApiException(ErrorCode.USER_DUPLICATE_EMAIL, Map.of("email","duplicate"));
        }

        User user = new User(
            req.email(),
            passwordEncoder.encode(req.password()),
            req.name(),
            LocalDate.parse(req.birthdate()),
            UserGender.valueOf(req.gender()),
            UserRole.ROLE_USER,
            null,
            req.address()
        );

        User saved = userRepository.save(user);
        return new UserResponse(saved.getId(), saved.getEmail(), saved.getName(), saved.getRole().name());
    }

    public UserResponse get(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        return new UserResponse(user.getId(), user.getEmail(), user.getName(), user.getRole().name());
    }

    public UserResponse me(UserPrincipal principal) {
    User user = userRepository.findById(principal.userId()).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
    return new UserResponse(user.getId(), user.getEmail(), user.getName(), user.getRole().name());
    }

    public UserResponse update(Long id, com.minsu.webservice.domain.user.dto.UpdateUserRequest req, UserPrincipal principal) {
        User user = userRepository.findById(id).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        boolean isAdmin = "ADMIN".equals(principal.role());
        if (!isAdmin && principal.userId() != id) throw new ApiException(ErrorCode.FORBIDDEN);

        user.update(
            req.name(),
            req.birthdate() == null ? null : LocalDate.parse(req.birthdate()),
            req.gender() == null ? null : UserGender.valueOf(req.gender()),
            req.address(),
            req.phonenumber()
        );

        if (req.password() != null && !req.password().isBlank()) {
            user.changePassword(passwordEncoder.encode(req.password()));
        }

        User saved = userRepository.save(user);
        return new UserResponse(saved.getId(), saved.getEmail(), saved.getName(), saved.getRole().name());
    }

    public void delete(Long id, UserPrincipal principal) {
        User user = userRepository.findById(id).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        boolean isAdmin = "ADMIN".equals(principal.role());
        if (!isAdmin && principal.userId() != id) throw new ApiException(ErrorCode.FORBIDDEN);
        userRepository.delete(user);
    }

    public Page<UserResponse> adminList(String keyword, String role, com.minsu.webservice.global.dto.PageRequest pageRequest, com.minsu.webservice.global.dto.SortCriteria sortCriteria) {
        UserRole userRole = (role == null || role.isBlank()) ? null : UserRole.valueOf(role);
        
        Specification<User> spec = UserSpecification.search(keyword, userRole);
        Pageable pageable = pageRequest.toPageable(sortCriteria.toSort());

        Page<User> page = userRepository.findAll(spec, pageable);

        return page.map(u -> new UserResponse(u.getId(), u.getEmail(), u.getName(), u.getRole().name()));
    }

    public void adminDeactivate(Long id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        user.deactivate();
        userRepository.save(user);
    }

    public void adminChangeRole(Long id, String role) {
        User user = userRepository.findById(id).orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        user.changeRole(UserRole.valueOf(role));
        userRepository.save(user);
    }

    public void deactivateByAdmin(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        user.deactivate();
        userRepository.save(user);
    }
}

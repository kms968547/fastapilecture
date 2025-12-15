package com.minsu.webservice.domain.auth;

import com.minsu.webservice.domain.auth.dto.LoginRequest;
import com.minsu.webservice.domain.auth.dto.RefreshRequest;
import com.minsu.webservice.global.error.ApiResponse;
import com.minsu.webservice.global.security.UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<?>> login(@Valid @RequestBody LoginRequest req, HttpServletRequest request) {
        String ua = request.getHeader("User-Agent");
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isBlank()) {
            ip = request.getRemoteAddr();
        }
        var payload = authService.login(req, ua, ip);
        return ResponseEntity.ok(ApiResponse.ok("AUTH_LOGIN_OK", "로그인 성공!", payload));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<?>> refresh(@Valid @RequestBody RefreshRequest req) {
        var payload = authService.refresh(req);
        return ResponseEntity.ok(ApiResponse.ok("AUTH_REFRESH_OK", "토큰 재발급 완료", payload));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<?>> logout(Authentication authentication) {
        Object principal = authentication == null ? null : authentication.getPrincipal();
        if (principal instanceof UserPrincipal up) {
            authService.logout(up.userId());
        }
        return ResponseEntity.ok(ApiResponse.ok("AUTH_LOGOUT_OK", "로그아웃 완료"));
    }
}

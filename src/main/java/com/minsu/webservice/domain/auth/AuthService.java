package com.minsu.webservice.domain.auth;

import com.minsu.webservice.domain.auth.dto.LoginPayload;
import com.minsu.webservice.domain.auth.dto.LoginRequest;
import com.minsu.webservice.domain.auth.dto.RefreshPayload;
import com.minsu.webservice.domain.auth.dto.RefreshRequest;
import com.minsu.webservice.domain.user.User;
import com.minsu.webservice.domain.user.UserRepository;
import com.minsu.webservice.global.error.ErrorCode;
import com.minsu.webservice.global.exception.ApiException;
import com.minsu.webservice.global.security.JwtProvider;
import com.minsu.webservice.global.security.TokenHasher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public AuthService(UserRepository userRepository, RefreshTokenRepository refreshTokenRepository, PasswordEncoder passwordEncoder, JwtProvider jwtProvider) {
        this.userRepository = userRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtProvider = jwtProvider;
    }

    @Transactional
    public LoginPayload login(LoginRequest req, String userAgent, String ip) {
        User user = userRepository.findByEmail(req.email())
                .orElseThrow(() -> new ApiException(ErrorCode.AUTH_INVALID_CREDENTIALS));
        if (!user.isActive()) {
            throw new ApiException(ErrorCode.USER_DEACTIVATED);
        }

        if (!passwordEncoder.matches(req.password(), user.getPassword())) {
            throw new ApiException(ErrorCode.AUTH_INVALID_CREDENTIALS);
        }

        String accessToken = jwtProvider.createAccessToken(user.getId(), user.getEmail(), user.getRole().name());
        String refreshToken = UUID.randomUUID().toString().replace("-", "") + UUID.randomUUID().toString().replace("-", "");
        String hash = TokenHasher.sha256(refreshToken);

        Instant expiresAt = Instant.now().plusSeconds(jwtProvider.refreshTtlSec());
        RefreshToken entity = new RefreshToken(user, hash, expiresAt, userAgent, ip);
        refreshTokenRepository.save(entity);

        return new LoginPayload(
                accessToken,
                refreshToken,
                jwtProvider.accessExpiresInSec(),
                user.getId(),
                user.getRole().name(),
                "Bearer"
        );
    }

    @Transactional
    public RefreshPayload refresh(RefreshRequest req) {
        String hash = TokenHasher.sha256(req.refreshToken());
        RefreshToken token = refreshTokenRepository.findByTokenHash(hash)
                .orElseThrow(() -> new ApiException(ErrorCode.REFRESH_TOKEN_INVALID));

        Instant now = Instant.now();
        if (token.isRevoked()) {
            throw new ApiException(ErrorCode.REFRESH_TOKEN_REVOKED);
        }
        if (token.isExpired(now)) {
            throw new ApiException(ErrorCode.REFRESH_TOKEN_EXPIRED);
        }

        User user = token.getUser();

        token.revoke(now);

        String newAccessToken = jwtProvider.createAccessToken(user.getId(), user.getEmail(), user.getRole().name());
        String newRefreshToken = UUID.randomUUID().toString().replace("-", "") + UUID.randomUUID().toString().replace("-", "");
        String newHash = TokenHasher.sha256(newRefreshToken);

        Instant newExpiresAt = now.plusSeconds(jwtProvider.refreshTtlSec());
        RefreshToken newEntity = new RefreshToken(user, newHash, newExpiresAt, token.getUserAgent(), token.getIp());
        refreshTokenRepository.save(newEntity);

        return new RefreshPayload(newAccessToken, newRefreshToken, jwtProvider.accessExpiresInSec(), "Bearer");
    }

    @Transactional
    public void logout(long userId) {
        refreshTokenRepository.revokeAllActiveByUserId(userId, Instant.now());
    }
}

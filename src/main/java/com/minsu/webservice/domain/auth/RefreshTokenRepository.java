package com.minsu.webservice.domain.auth;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByTokenHash(String tokenHash);

    @Modifying
    @Query("update RefreshToken rt set rt.revokedAt = :now where rt.user.id = :userId and rt.revokedAt is null and rt.expiresAt > :now")
    int revokeAllActiveByUserId(@Param("userId") long userId, @Param("now") Instant now);
}

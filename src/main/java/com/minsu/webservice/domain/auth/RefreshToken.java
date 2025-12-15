package com.minsu.webservice.domain.auth;

import com.minsu.webservice.domain.user.User;
import jakarta.persistence.*;

import java.time.Instant;

@Entity
@Table(name = "`Refresh_token`", indexes = {
        @Index(name = "idx_refresh_token_user_id", columnList = "user_id"),
        @Index(name = "idx_refresh_token_token_hash", columnList = "token_hash", unique = true)
})
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "token_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "token_hash", nullable = false, unique = true, length = 255)
    private String tokenHash;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "revoked_at")
    private Instant revokedAt;

    @Column(name = "user_agent", length = 255)
    private String userAgent;

    @Column(name = "ip", length = 64)
    private String ip;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    protected RefreshToken() {}

    public RefreshToken(User user, String tokenHash, Instant expiresAt, String userAgent, String ip) {
        this.user = user;
        this.tokenHash = tokenHash;
        this.expiresAt = expiresAt;
        this.userAgent = userAgent;
        this.ip = ip;
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public User getUser() { return user; }
    public String getUserAgent() { return userAgent; }
    public String getIp() { return ip; }
    public String getTokenHash() { return tokenHash; }
    public Instant getExpiresAt() { return expiresAt; }
    public Instant getRevokedAt() { return revokedAt; }
    public boolean isRevoked() { return revokedAt != null; }
    public boolean isExpired(Instant now) { return expiresAt.isBefore(now) || expiresAt.equals(now); }
    public void revoke(Instant now) { this.revokedAt = now; }
}

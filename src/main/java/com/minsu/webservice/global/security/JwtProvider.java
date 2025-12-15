package com.minsu.webservice.global.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Base64; // Added import
import javax.crypto.SecretKey;

    public class JwtProvider {

    private final JwtProperties props;
    private final SecretKey key;

    public JwtProvider(JwtProperties props) {
        this.props = props;
        byte[] keyBytes = Base64.getDecoder().decode(props.secret());
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(long userId, String email, String role) {
        Instant now = Instant.now();
        Instant exp = now.plusSeconds(props.accessTtlSec());
        return Jwts.builder()
                .setSubject(email)
                .claim("uid", userId)
                .claim("role", role)
                .setIssuedAt(Date.from(now))
                .setExpiration(Date.from(exp))
                .signWith(key)
                .compact();
    }

    public long accessExpiresInSec() {
        return props.accessTtlSec();
    }

    public long refreshTtlSec() {
        return props.refreshTtlSec();
    }

    public Claims parse(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}

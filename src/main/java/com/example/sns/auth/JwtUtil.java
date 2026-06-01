package com.example.sns.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JwtUtil {

    private final SecretKey secretKey;
    private final long accessTokenExpireTime;

    public JwtUtil(
            @Value("${jwt.secret-key}") String secretKey,
            @Value("${jwt.access-token-expire-time}") long accessTokenExpireTime
    ) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpireTime = accessTokenExpireTime;
    }

    // Access Token 생성
    public String createAccessToken(Long userId, String email) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + accessTokenExpireTime);

        return Jwts.builder()
                .subject(String.valueOf(userId))
                .claim("email", email)
                .issuedAt(now)
                .expiration(expiration)
                .signWith(secretKey)
                .compact();
    }

    // 토큰에서 userId 추출
    public Long getUserId(String token) {
        Claims claims = parseClaims(token);

        return Long.parseLong(claims.getSubject());
    }

    // 토큰에서 email 추출
    public String getEmail(String token) {
        Claims claims = parseClaims(token);

        return claims.get("email", String.class);
    }

    // 토큰 유효성 검사
    public boolean validateToken(String token) {
        try {
            parseClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    // JWT payload 부분 해석
    private Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}
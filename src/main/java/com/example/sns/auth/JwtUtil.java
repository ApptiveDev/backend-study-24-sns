package com.example.sns.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final JwtProperties jwtProperties;

    // Access Token 생성
    public String generateAccessToken(Long userId) {
        return Jwts.builder()
                .subject(String.valueOf(userId))
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiration()))
                .signWith(getSecretKey())
                .compact();
    }

    // Refresh Token 생성 (단순 문자열)
    public String generateRefreshToken() {
        return UUID.randomUUID().toString();
    }

    // Refresh Token 만료시간 계산
    public LocalDateTime getRefreshTokenExpiresAt() {
        return LocalDateTime.now().plusDays(jwtProperties.getRefreshExpiration());
    }

    // 토큰 파싱 (만료/서명오류 등 예외를 그대로 던짐 → 호출부에서 구분해서 처리)
    public Claims parseClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    // 토큰에서 userId 추출
    public Long getUserId(String token) {
        return Long.valueOf(parseClaims(token).getSubject());
    }

    // SecretKey를 매번 생성하지 않으려면 @PostConstruct로 캐싱도 가능
    private SecretKey getSecretKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecret().getBytes(StandardCharsets.UTF_8));
    }
}

package com.example.sns.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtProvider {

    private final Key secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    private final long ACCESS_TOKEN_VALID_TIME = 1000L * 60 * 60;

    // 1. 로그인 성공 시 Access Token 발급
    public String createAccessToken(Long userId) {
        Date now = new Date();
        return Jwts.builder()
                .setSubject(String.valueOf(userId)) // Payload에 userId 저장
                .setIssuedAt(now) // 발급 시간
                .setExpiration(new Date(now.getTime() + ACCESS_TOKEN_VALID_TIME)) // 만료 시간
                .signWith(secretKey) // 비밀 키로 서명 (위조 방지)
                .compact();
    }

    // 2. 토큰이 진짜인지(위조/만료) 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            // 만료되었거나 손상된 토큰이면 여기서 false를 반환합니다.
            return false;
        }
    }

    // 3. 토큰을 열어서 안에 들어있는 userId 불러옴
    public Long getUserIdFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(claims.getSubject());
    }
}
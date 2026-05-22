package com.example.sns.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Refresh Token을 소유한 사용자 id
    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId;

    // 실제 Refresh Token 값
    @Column(nullable = false, unique = true)
    private String token;

    private RefreshToken(Long userId, String token) {
        this.userId = userId;
        this.token = token;
    }

    public static RefreshToken create(Long userId, String token) {
        return new RefreshToken(userId, token);
    }

    public void updateToken(String token) {
        this.token = token;
    }
}
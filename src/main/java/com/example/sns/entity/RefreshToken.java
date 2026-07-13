package com.example.sns.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "refresh_tokens")
public class RefreshToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(nullable = false)
    private String token;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime expiresAt;

    // 외부에서 직접 new로 생성하지 못하도록 private 생성자 선언
    private RefreshToken(User user, String token, LocalDateTime expiresAt) {
        this.user = user;
        this.token = token;
        this.expiresAt = expiresAt;
    }

    // 리프레시 토큰 객체 생성을 담당하는 public static 팩토리 메서드
    public static RefreshToken create(User user, String token, LocalDateTime expiresAt) {
        if (user == null) {
            throw new IllegalArgumentException("사용자 ID는 필수입니다.");
        }
        if (token == null || token.isBlank()) {
            throw new IllegalArgumentException("토큰은 필수입니다.");
        }
        if (expiresAt == null) {
            throw new IllegalArgumentException("만료시간은 필수입니다.");
        }
        return new RefreshToken(user, token, expiresAt);
    }

    // 토큰 갱신
    public void updateToken(String token, LocalDateTime expiresAt) {
        this.token = token;
        this.expiresAt = expiresAt;
    }

    // 만료 여부 확인
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiresAt);
    }
}

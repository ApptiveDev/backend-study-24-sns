package com.example.sns.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 사용자 이메일
    @Column(nullable = false, length = 100)
    private String email;

    // 사용자 닉네임
    @Column(nullable = false, length = 50)
    private String nickname;

    // 사용자 비밀번호
    @Column(nullable = false)
    private String password;

    // 사용자 생성 시간
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    private User(String email, String nickname, String password) {
        this.email = email;
        this.nickname = nickname;
        this.password = password;
    }

    public static User create(String email, String nickname, String password) {
        return new User(email, nickname, password);
    }

    @PrePersist
    private void prePersist() {
        this.createdAt = LocalDateTime.now();
    }
}
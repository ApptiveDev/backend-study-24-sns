package com.example.sns.entity;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String nickname;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    protected User() {
    }

    private User(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
        this.createdAt = LocalDateTime.now();
    }

    public static User create(String email, String nickname) {
        return new User(email, nickname);
    }
}
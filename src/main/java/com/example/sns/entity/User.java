package com.example.sns.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 외부에서 무분별한 생성을 막음
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String email;

    // JWT 인증(로그인)을 위해 반드시 필요한 비밀번호 필드 추가
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    // 생성자 업데이트: 비밀번호를 포함하여 안전하게 객체 생성
    public User(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }
}
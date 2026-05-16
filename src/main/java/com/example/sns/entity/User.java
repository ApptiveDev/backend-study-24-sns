package com.example.sns.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

// Entity(DB에 저장할 데이터 형식을 나타내는 클래스)임을 선언하는 어노테이션
@Entity
// Getter method를 자동 생성하는 어노테이션
@Getter
// 인자 없는 default constructor 자동 생성하는 어노테이션 (protected User(){} 와 같은 역할)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// DB테이블의 이름을 정하는 어노테이션 (여기선 users로 설정)
@Table(name = "users")
public class User {

    // 이 변수가 테이블의 기본 키라 선언하는 어노테이션
    @Id
    // 이 변수는 자동으로 값을 생성한다는 어노테이션 (여기선 DB가 자동으로 1씩 올려서 채움)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 이 변수가 테이블의 column(열)임을 선언하는 어노테이션
    // (세부설정으로 테이블에서 이 열의 이름을 user_id로 설정, 생략시 변수명과 같음)
    @Column(name = "user_id")
    private Long id;

    // (세부설정으로 null을 허용하지 않도록 설정)
    @Column(nullable = false)
    private String username;

    // (세부설정으로 null을 허용하지 않고, 중복을 허용하지 않도록 설정)
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    // 양방향 1:N 관계 매핑 어노테이션
    // (사용자 한 명이 여러 게시글을 가짐, 주인이 아닌 쪽이므로 mappedBy로 주인 지정)
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Post> posts = new ArrayList<>();

    // 테스트 및 데이터 생성을 위한 생성자
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}

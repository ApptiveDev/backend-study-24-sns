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
// JPA가 DB 조회하여 리플렉션(자바 런타임에서 클래스의 구조를 검사하고 동적으로 객체를 생성)하므로 꼭 필요함.
// JPA가 필요로 하는 최소한 접근 가능하도록 protected 설정
// public 설정 시 아무 값도 없는 불완전한 User 객체 외부에서 생성 가능 (User user = new User();)
// private 설정 시 JPA가 프록시 객체 만들 때 엔티티 상속 불가
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

    // 외부에서 직접 new로 생성하지 못하도록 private 생성자 선언
    private User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    // 유저 객체 생성을 담당하는 public static 팩토리 메서드
    public static User create(String username, String email, String password) {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("이름은 필수입니다.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("이메일은 필수입니다.");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("비밀번호는 필수입니다.");
        }
        return new User(username, email, password);
    }
}

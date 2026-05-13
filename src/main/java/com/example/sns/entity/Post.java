package com.example.sns.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

// 중복되는 어노테이션 설명은 User에 있음
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "posts")
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    // (세부설정에서 DB의 열 타입을 TEXT로 지정, 수만 글자 이상 저장 가능)
    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @CreatedDate // 생성 시 자동 저장
    @Column(updatable = false) // 생성 시간은 수정 불가능하게 설정
    private LocalDateTime createdAt;

    @LastModifiedDate // 수정 시 자동 저장
    private LocalDateTime updatedAt;

    // 양방향 N:1 관계 매핑 어노테이션 (여러 게시글을 사용자 한 명이 가짐)
    @ManyToOne(fetch = FetchType.LAZY)
    // DB테이블에서 외래키(FK)의 이름을 user_id로 지정하는 어노테이션
    @JoinColumn(name = "user_id")
    private User user;

    // 게시글 생성을 위한 생성자
    public Post(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    // 수정 기능을 위한 함수
    // 제목만 수정
    public void updateTitle(String title) {
        if (title != null && !title.isBlank()) {
            this.title = title;
        }
    }

    // 내용만 수정
    public void updateContent(String content) {
        if (content != null && !content.isBlank()) {
            this.content = content;
        }
    }
}

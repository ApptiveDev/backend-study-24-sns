package com.example.sns.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(
        // like는 SQL 예약어라 likes로 테이블명 변경
        name = "likes",
        // 같은 유저가 같은 게시글에 중복 좋아요 제약
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "post_id"})
)
public class Like {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "like_id")
    private Long id;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // 단방향 N:1 관계 매핑 어노테이션 (여러 좋아요를 사용자 한 명이 가짐)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 단방향 N:1 관계 매핑 어노테이션 (여러 좋아요를 하나의 게시글이 가짐)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public Like(User user, Post post) {
        this.user = user;
        this.post = post;
    }
}

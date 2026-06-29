package com.example.sns.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
// JPA에서 엔티티가 생성되거나 수정될 때 발생하는 이벤트(생성, 수정 등)를 감지하여 자동으로 DB에 기록해주는 어노테이션
// 시간 자동 기록 이외에 다양한 전처리 로직(데이터 암호화 등)을 구현하려면 Auditing 대신 @PrePersist / @PreUpdate 필요
@EntityListeners(AuditingEntityListener.class)
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "comment_id")
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @CreatedDate // 생성될 때 시간 자동 주입 (Auditing)
    // Auditing 대신 @PrePersist 사용 시 DB에 저장 직전 실행되며, 직접 코드 작성 필요
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate // 수정될 때 시간 자동 주입 (Auditing)
    // Auditing 대신 @PreUdate 사용 시 DB에 수정 직전 실행되며, 직접 코드 작성 필요
    private LocalDateTime updatedAt;

    // 단방향 N:1 관계 매핑 어노테이션 (여러 댓글을 사용자 한 명이 가짐)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    // 단방향 N:1 관계 매핑 어노테이션 (여러 댓글을 하나의 게시글이 가짐)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public Comment(String content, User user, Post post) {
        this.content = content;
        this.user = user;
        this.post = post;
    }

    // 값 검증 및 수정
    public void update(String content) {
        if (content != null && !content.isBlank()) {
            this.content = content;
        }
    }
}

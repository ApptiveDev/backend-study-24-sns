package com.example.sns.post.entity;

import com.example.sns.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private Post(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
        this.createdAt = LocalDateTime.now();
    }

    public static Post createPost(String title, String content, User user) {
        validateTitle(title);
        validateContent(content);

        return new Post(title, content, user);
    }


    public void update(String title, String content) {  // 변경 로직을 엔티티 내부에 캡슐화
        validateTitle(title);
        validateContent(content);
        this.title = title;
        this.content = content;
        this.updatedAt = LocalDateTime.now(); // 자동으로 함께 업데이트
    }


    private static void validateTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목은 필수입니다.");
        }
        if (title.length() > 100) {
            throw new IllegalArgumentException("제목은 100자 이하여야 합니다.");
        }
    }

    private static void validateContent(String content) {
        if (content == null || content.isBlank()) {
           throw new IllegalArgumentException("내용은 필수입니다.");
        }
    }
}

package com.example.sns.comment.entity;

import com.example.sns.post.entity.Post;
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
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Column
    private LocalDateTime createdAt;

    @Column
    private LocalDateTime updatedAt;


    public Comment(String content, User user, Post post) {
        this.content = content;
        this.user = user;
        this.post = post;
        this.createdAt = LocalDateTime.now();
    }

    public static Comment createComment(String content, User user, Post post) {
        validateContent(content);

        return new Comment(content, user, post);
    }

    public void update(String content) {
        this.content = content;
        this.updatedAt = LocalDateTime.now();
    }


    private static void validateContent(String content) {
        if (content == null || content.isEmpty()) {
            throw new IllegalArgumentException("내용은 필수입니다.");
        }
    }
}

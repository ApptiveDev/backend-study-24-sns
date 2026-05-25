package com.example.sns.entity;

import com.example.sns.exception.ForbiddenException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "comments")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Comment(String content, Post post, User author) {
        this.content = content;
        this.post = post;
        this.author = author;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    public static Comment create(String content, Post post, User author) {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("댓글 내용은 필수입니다.");
        }

        if (post == null) {
            throw new IllegalArgumentException("게시글은 필수입니다.");
        }

        if (author == null) {
            throw new IllegalArgumentException("작성자는 필수입니다.");
        }

        return new Comment(content, post, author);
    }

    public void edit(String newContent, User requester) {
        validateAuthor(requester);

        if (newContent == null || newContent.isBlank()) {
            throw new IllegalArgumentException("댓글 내용은 필수입니다.");
        }

        this.content = newContent;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean isWrittenBy(User user) {
        return this.author.isSameUser(user);
    }

    private void validateAuthor(User requester) {
        if (!isWrittenBy(requester)) {
            throw new ForbiddenException("댓글 작성자만 수정할 수 있습니다.");
        }
    }


}

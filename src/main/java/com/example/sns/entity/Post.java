package com.example.sns.entity;

import com.example.sns.exception.CommentNotFoundException;
import com.example.sns.exception.ForbiddenException;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Entity
@Getter
@Table(name = "posts")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id")
    private User author;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Like> likes = new ArrayList<>();

    private Post(User author, String title, String content) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.createdAt = LocalDateTime.now();
    }

    public static Post create(User author, String title, String content) {
        if (author == null) {
            throw new IllegalArgumentException("작성자는 필수입니다.");
        }
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목은 필수입니다.");
        }
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("내용은 필수입니다.");
        }
        return new Post(author, title, content);
    }

    public void update(String title, String content) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("제목은 필수입니다.");
        }

        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("내용은 필수입니다.");
        }

        this.title = title;
        this.content = content;
    }

    public Comment addComment(String content, User commenter) {
        Comment comment = Comment.create(content, this, commenter);
        this.comments.add(comment);
        return comment;
    }

    public Comment editComment(Long commentId, String newContent, User requester) {
        Comment comment = findComment(commentId);
        comment.edit(newContent, requester);
        return comment;
    }

    public void removeComment(Long commentId, User requester) {
        Comment comment = findComment(commentId);

        if (!comment.isWrittenBy(requester) && !this.isWrittenBy(requester)) {
            throw new ForbiddenException("댓글 삭제 권한이 없습니다.");
        }

        this.comments.remove(comment);
    }

    public List<Comment> getComments() {
        return Collections.unmodifiableList(comments);
    }

    private Comment findComment(Long commentId) {
        return this.comments.stream()
                .filter(comment -> comment.getId().equals(commentId))
                .findFirst()
                .orElseThrow(CommentNotFoundException::new);
    }

    public boolean isWrittenBy(User user) {
        return this.author.isSameUser(user);
    }

}

package com.example.sns.entity;

import com.example.sns.exception.CustomException;
import com.example.sns.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    public Comment(String content, User user, Post post) {
        this.content = content;
        this.user = user;
        this.post = post;
    }

    // 작성자 본인인지 확인한다.
    public boolean isWrittenBy(User user) {
        return this.user.getId().equals(user.getId());
    }

    // 권한을 검증하고 댓글 내용을 수정한다.
    public void update(String content, User requester) {
        if (!isWrittenBy(requester)) {
            throw new CustomException(ErrorCode.NO_PERMISSION);
        }
        this.content = content;
    }
}
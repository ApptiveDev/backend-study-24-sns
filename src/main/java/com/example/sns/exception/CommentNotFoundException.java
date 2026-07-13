package com.example.sns.exception;

import lombok.Getter;

@Getter
public class CommentNotFoundException extends RuntimeException {
    private final ErrorCode errorCode = ErrorCode.COMMENT_NOT_FOUND;

    public CommentNotFoundException(Long commentId) {
        super(ErrorCode.COMMENT_NOT_FOUND.getMessage() + " commentId=" + commentId);
    }
}

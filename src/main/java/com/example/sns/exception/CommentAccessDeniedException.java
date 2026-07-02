package com.example.sns.exception;

import lombok.Getter;

@Getter
public class CommentAccessDeniedException extends RuntimeException {
    private final ErrorCode errorCode = ErrorCode.COMMENT_FORBIDDEN;

    public CommentAccessDeniedException() {
        super(ErrorCode.COMMENT_FORBIDDEN.getMessage());
    }
}

package com.example.sns.exception;

import lombok.Getter;

@Getter
public class PostAccessDeniedException extends RuntimeException {
    private final ErrorCode errorCode = ErrorCode.POST_FORBIDDEN;

    public PostAccessDeniedException() {
        super(ErrorCode.POST_FORBIDDEN.getMessage());
    }
}

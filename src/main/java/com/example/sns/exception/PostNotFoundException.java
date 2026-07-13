package com.example.sns.exception;

import lombok.Getter;

@Getter
public class PostNotFoundException extends RuntimeException {
    private final ErrorCode errorCode = ErrorCode.POST_NOT_FOUND;

    public PostNotFoundException(Long postId) {
        super(ErrorCode.POST_NOT_FOUND.getMessage() + " postId=" + postId);
    }
}

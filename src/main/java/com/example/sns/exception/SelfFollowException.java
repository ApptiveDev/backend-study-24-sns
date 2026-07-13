package com.example.sns.exception;

import lombok.Getter;

@Getter
public class SelfFollowException extends RuntimeException {
    private final ErrorCode errorCode = ErrorCode.SELF_FOLLOW_NOT_ALLOWED;

    public SelfFollowException() {
        super(ErrorCode.SELF_FOLLOW_NOT_ALLOWED.getMessage());
    }
}

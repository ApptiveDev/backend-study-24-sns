package com.example.sns.exception;

import lombok.Getter;

@Getter
public class UserNotFoundException extends RuntimeException {
    private final ErrorCode errorCode = ErrorCode.USER_NOT_FOUND;

    public UserNotFoundException(Long userId) {
        super(ErrorCode.USER_NOT_FOUND.getMessage() + " userId=" + userId);
    }
}

package com.example.sns.exception;

import lombok.Getter;

@Getter
public class InvalidLoginException extends RuntimeException {
    private final ErrorCode errorCode = ErrorCode.INVALID_LOGIN;

    public InvalidLoginException() {
        super(ErrorCode.INVALID_LOGIN.getMessage());
    }
}

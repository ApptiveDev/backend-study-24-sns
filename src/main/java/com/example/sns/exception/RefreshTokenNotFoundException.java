package com.example.sns.exception;

import lombok.Getter;

@Getter
public class RefreshTokenNotFoundException extends RuntimeException {
    private final ErrorCode errorCode = ErrorCode.REFRESH_TOKEN_NOT_FOUND;

    public RefreshTokenNotFoundException() {
        super(ErrorCode.REFRESH_TOKEN_NOT_FOUND.getMessage());
    }
}

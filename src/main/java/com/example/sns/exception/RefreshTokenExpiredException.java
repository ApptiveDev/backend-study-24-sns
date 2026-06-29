package com.example.sns.exception;

import lombok.Getter;

@Getter
public class RefreshTokenExpiredException extends RuntimeException {
    private final ErrorCode errorCode = ErrorCode.REFRESH_TOKEN_EXPIRED;

    public RefreshTokenExpiredException() {
        super(ErrorCode.REFRESH_TOKEN_EXPIRED.getMessage());
    }
}

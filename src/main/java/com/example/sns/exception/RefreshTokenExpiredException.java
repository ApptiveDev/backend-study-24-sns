package com.example.sns.exception;

public class RefreshTokenExpiredException extends RuntimeException {
    public RefreshTokenExpiredException() {
        super("만료된 Refresh Token입니다. 다시 로그인해주세요.");
    }
}

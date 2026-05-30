package com.example.sns.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(Long userId) {
        super("해당 유저를 찾을 수 없습니다. userId=" + userId);
    }
}

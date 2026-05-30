package com.example.sns.exception;

public class SelfFollowException extends RuntimeException {
    public SelfFollowException() {
        super("자기 자신을 팔로우할 수 없습니다.");
    }
}

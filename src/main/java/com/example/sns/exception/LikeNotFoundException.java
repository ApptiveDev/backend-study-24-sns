package com.example.sns.exception;

public class LikeNotFoundException extends RuntimeException {
    public LikeNotFoundException() {
        super("좋아요를 누른 기록이 없습니다.");
    }
}

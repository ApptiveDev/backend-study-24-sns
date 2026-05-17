package com.example.sns.exception;

public class AlreadyLikedException extends RuntimeException {
    public AlreadyLikedException() {
        super("이미 좋아요를 누른 게시글입니다.");
    }
}

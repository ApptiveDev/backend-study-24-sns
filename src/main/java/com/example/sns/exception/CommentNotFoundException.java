package com.example.sns.exception;

public class CommentNotFoundException extends RuntimeException {
    public CommentNotFoundException() {
        super("해당 게시글에 존재하지 않는 댓글입니다.");
    }
}

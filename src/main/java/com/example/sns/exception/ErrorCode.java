package com.example.sns.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    // User 관련
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, "이미 존재하는 이메일입니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "이메일 또는 비밀번호가 틀렸습니다."),

    // JWT/Auth 관련
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다."),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "만료된 토큰입니다."),
    INVALID_REFRESH_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 Refresh Token입니다."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "Refresh Token을 찾을 수 없습니다."),
    TOKEN_MISSING(HttpStatus.UNAUTHORIZED, "토큰이 없습니다."),

    // Post 관련
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "게시물을 찾을 수 없습니다."),
    POST_FORBIDDEN(HttpStatus.FORBIDDEN, "본인의 게시물만 수정/삭제할 수 있습니다."),

    // Comment 관련
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "댓글을 찾을 수 없습니다."),
    COMMENT_FORBIDDEN(HttpStatus.FORBIDDEN, "본인의 댓글만 수정/삭제할 수 있습니다."),

    // Like 관련
    ALREADY_LIKED(HttpStatus.CONFLICT, "이미 좋아요를 누른 게시물입니다."),

    // Validation
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "입력값이 올바르지 않습니다.");

    private final HttpStatus status;
    private final String message;
}

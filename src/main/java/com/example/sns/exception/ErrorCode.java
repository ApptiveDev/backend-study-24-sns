package com.example.sns.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User does not exist."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "Post does not exist."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Comment does not exist."),
    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "Like does not exist."),
    LIKE_ALREADY_EXISTS(HttpStatus.BAD_REQUEST, "Like already exists."),
    DUPLICATE_USERNAME(HttpStatus.BAD_REQUEST, "Username already exists."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "Authentication is required."),
    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "Invalid token."),
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "Refresh token does not exist."),
    REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "Refresh token has expired."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "You do not have permission.");

    private final HttpStatus status;
    private final String message;

    ErrorCode(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}

package com.example.sns.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PostNotFoundException.class)
    public ResponseEntity<ErrorResponse> handlePostNotFoundException(
            PostNotFoundException e
    ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("POST_NOT_FOUND", e.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleUserNotFoundException(
            UserNotFoundException e
    ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("USER_NOT_FOUND", e.getMessage()));
    }

    @ExceptionHandler(CommentNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleCommentNotFoundException(
            CommentNotFoundException e
    ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("COMMENT_NOT_FOUND", e.getMessage()));
    }

    @ExceptionHandler(LikeNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleLikeNotFoundException(
            LikeNotFoundException e
    ) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErrorResponse("LIKE_NOT_FOUND", e.getMessage()));
    }

    @ExceptionHandler(AlreadyLikedException.class)
    public ResponseEntity<ErrorResponse> handleAlreadyLikedException(
            AlreadyLikedException e
    ) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("ALREADY_LIKED", e.getMessage()));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ErrorResponse> handleForbiddenException(
            ForbiddenException e
    ) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErrorResponse("FORBIDDEN", e.getMessage()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException e
    ) {
        return ResponseEntity.badRequest()
                .body(new ErrorResponse("BAD_REQUEST", e.getMessage()));
    }

    @ExceptionHandler(DuplicateEmailException.class)
    public ResponseEntity<ErrorResponse> handleDuplicateEmailException(
            DuplicateEmailException e
    ) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErrorResponse("DUPLICATE_EMAIL", e.getMessage()));
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<ErrorResponse> handleInvalidPasswordException(
            InvalidPasswordException e
    ) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErrorResponse("INVALID_PASSWORD", e.getMessage()));
    }
}
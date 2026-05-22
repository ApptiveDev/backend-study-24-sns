package com.example.sns.controller;

import com.example.sns.auth.AuthInterceptor;
import com.example.sns.dto.CommentCreateRequest;
import com.example.sns.dto.CommentResponse;
import com.example.sns.dto.CommentUpdateRequest;
import com.example.sns.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/comments")
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponse> create(
            @RequestAttribute(AuthInterceptor.AUTH_USER_ID) Long authUserId,
            @Valid @RequestBody CommentCreateRequest request
    ) {
        CommentResponse response = commentService.create(authUserId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> findAllByPostId(@RequestParam Long postId) {
        return ResponseEntity.ok(commentService.findAllByPostId(postId));
    }

    @GetMapping("/{commentId}")
    public ResponseEntity<CommentResponse> findById(@PathVariable Long commentId) {
        return ResponseEntity.ok(commentService.findById(commentId));
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponse> update(
            @PathVariable Long commentId,
            @RequestAttribute(AuthInterceptor.AUTH_USER_ID) Long authUserId,
            @Valid @RequestBody CommentUpdateRequest request
    ) {
        return ResponseEntity.ok(commentService.update(commentId, authUserId, request));
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long commentId,
            @RequestAttribute(AuthInterceptor.AUTH_USER_ID) Long authUserId
    ) {
        commentService.delete(commentId, authUserId);
        return ResponseEntity.noContent().build();
    }
}

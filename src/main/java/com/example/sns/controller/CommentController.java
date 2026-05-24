package com.example.sns.controller;

import com.example.sns.dto.CommentRequest;
import com.example.sns.dto.CommentResponse;
import com.example.sns.service.CommentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable Long postId,
            HttpServletRequest httpRequest,
            @Valid @RequestBody CommentRequest request) {

        Long userId = (Long) httpRequest.getAttribute("userId");
        CommentResponse response = commentService.createComment(postId, userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long postId, // 경로 검증용으로 유지
            @PathVariable Long commentId,
            HttpServletRequest httpRequest,
            @Valid @RequestBody CommentRequest request) {

        Long userId = (Long) httpRequest.getAttribute("userId");
        CommentResponse response = commentService.updateComment(commentId, userId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            HttpServletRequest httpRequest) {

        Long userId = (Long) httpRequest.getAttribute("userId");
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.noContent().build();
    }
}
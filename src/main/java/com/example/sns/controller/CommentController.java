package com.example.sns.controller;

import com.example.sns.dto.CommentRequest;
import com.example.sns.dto.CommentResponse;
import com.example.sns.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 1. 댓글 작성 (특정 게시글에 종속됨)
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable Long postId,
            @RequestBody CommentRequest request) {
        CommentResponse response = commentService.createComment(postId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 2. 특정 게시글의 댓글 전체 조회
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long postId) {
        List<CommentResponse> responses = commentService.getComments(postId);
        return ResponseEntity.ok(responses);
    }

    // 3. 댓글 수정
    @PatchMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long commentId,
            @RequestBody CommentRequest request) {
        CommentResponse response = commentService.updateComment(commentId, request);
        return ResponseEntity.ok(response);
    }

    // 4. 댓글 삭제
    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            @RequestParam Long userId) { // 삭제할 때는 쿼리 파라미터(?userId=1)로 유저 검증
        commentService.deleteComment(commentId, userId);
        return ResponseEntity.noContent().build();
    }
}
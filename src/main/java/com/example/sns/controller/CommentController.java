package com.example.sns.controller;

import com.example.sns.dto.CommentCreateRequestDto;
import com.example.sns.dto.CommentResponseDto;
import com.example.sns.dto.CommentUpdateRequestDto;
import com.example.sns.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    // 댓글 작성: POST /api/posts/1/comments
    @PostMapping
    public ResponseEntity<Void> create(@PathVariable Long postId, @RequestBody @Valid CommentCreateRequestDto dto) {
        commentService.createComment(postId, dto);
        return ResponseEntity.status(HttpStatus.CREATED).build(); // 201
    }

    // 댓글 조회: GET /api/posts/1/comments
    @GetMapping
    public ResponseEntity<List<CommentResponseDto>> list(@PathVariable Long postId) {
        return ResponseEntity.ok(commentService.getComments(postId)); // 200 + 데이터
    }

    // 특정 댓글 수정: PATCH /api/posts/1/comments/3
    @PatchMapping("/{commentId}")
    public ResponseEntity<Void> update(@PathVariable Long postId, @PathVariable Long commentId, @RequestBody @Valid CommentUpdateRequestDto dto) {
        commentService.updateComment(commentId, dto);
        return ResponseEntity.ok().build(); // 200 반환
    }

    // 특정 댓글 삭제: DELETE /api/posts/1/comments/3
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable Long postId, @PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return ResponseEntity.noContent().build(); // 204 반환
    }
}

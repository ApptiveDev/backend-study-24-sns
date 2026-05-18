package com.example.sns.post.controller;

import com.example.sns.comment.dto.CommentCreateRequest;
import com.example.sns.comment.dto.CommentResponse;
import com.example.sns.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class PostCommentController {

    private final CommentService commentService;

    // 댓글 생성
    @PostMapping
    public ResponseEntity<CommentResponse> create(@Valid @PathVariable Long postId, @Valid @RequestBody CommentCreateRequest request) {
        CommentResponse response = commentService.create(postId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    // 댓글 조회
    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments(@PathVariable Long postId) {
        List<CommentResponse> responses = commentService.findByPostId(postId);
        return ResponseEntity.ok(responses);
    }
}

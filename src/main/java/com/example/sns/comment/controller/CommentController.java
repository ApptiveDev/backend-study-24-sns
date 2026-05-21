package com.example.sns.comment.controller;

import com.example.sns.comment.dto.CommentResponse;
import com.example.sns.comment.dto.CommentUpdateRequest;
import com.example.sns.comment.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponse> createComment(@PathVariable Long commentId, @Valid @RequestBody CommentUpdateRequest request) {
        CommentResponse response = commentService.update(commentId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<CommentResponse> deleteComment(@PathVariable Long commentId) {
        commentService.delete(commentId);
        return ResponseEntity.ok().build();
    }

}

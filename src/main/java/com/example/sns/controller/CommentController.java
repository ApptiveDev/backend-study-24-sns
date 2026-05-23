package com.example.sns.controller;

import com.example.sns.dto.CommentCreateRequest;
import com.example.sns.dto.CommentResponse;
import com.example.sns.dto.CommentUpdateRequest;
import com.example.sns.service.CommentCommandService;
import com.example.sns.service.CommentQueryService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/posts/{postId}/comments")
public class CommentController {

    private final CommentCommandService commentCommandService;
    private final CommentQueryService commentQueryService;

    @PostMapping
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable Long postId,
            @RequestBody CommentCreateRequest request,
            HttpServletRequest httpRequest
    ) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        CommentResponse response = commentCommandService.addComment(postId, userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<CommentResponse>> getComments(
            @PathVariable Long postId
    ) {
        List<CommentResponse> response = commentQueryService.getComments(postId);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            @RequestBody CommentUpdateRequest request,
            HttpServletRequest httpRequest
    ) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        CommentResponse response = commentCommandService.editComment(postId, commentId, userId, request);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long postId,
            @PathVariable Long commentId,
            HttpServletRequest httpRequest
    ) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        commentCommandService.removeComment(postId, commentId, userId);
        return ResponseEntity.noContent().build();
    }
}
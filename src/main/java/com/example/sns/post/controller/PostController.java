package com.example.sns.post.controller;

import com.example.sns.comment.dto.CommentCreateRequest;
import com.example.sns.comment.dto.CommentResponse;
import com.example.sns.comment.service.CommentService;
import com.example.sns.post.dto.PostCreateRequest;
import com.example.sns.post.dto.PostDetailResponse;
import com.example.sns.post.dto.PostResponse;
import com.example.sns.post.service.PostService;
import com.example.sns.post.dto.PostUpdateRequest;
import com.example.sns.post.entity.Post;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    // 게시물 생성
    @PostMapping
    public ResponseEntity<PostResponse> create(@Valid @RequestBody PostCreateRequest request) {
        PostResponse response = postService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    // 전체 조회
    @GetMapping
    public ResponseEntity<Page<PostResponse>> findAll(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {
        Page<PostResponse> response = postService.findAll(page, size);
        return ResponseEntity.ok(response);
    }


    // Id별 조회
    @GetMapping("/{postId}")
    public ResponseEntity<PostDetailResponse> findById(@PathVariable Long postId) {
        PostDetailResponse response = postService.findByIdWithComments(postId);
        return ResponseEntity.ok(response);
    }


    // 수정
    @PatchMapping("/{postId}")
    public ResponseEntity<PostResponse> update(@PathVariable Long postId, @Valid @RequestBody PostUpdateRequest request) {
        PostResponse response = postService.update(postId, request);
        return ResponseEntity.ok(response);
    }


    // 삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<PostResponse> delete(@PathVariable Long postId) {
        postService.delete(postId);
        return ResponseEntity.noContent().build();
    }
}

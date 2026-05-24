package com.example.sns.controller;

import com.example.sns.dto.PostCreateRequest;
import com.example.sns.dto.PostResponse;
import com.example.sns.dto.PostUpdateRequest;
import com.example.sns.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    // 게시글을 생성한다.
    @PostMapping
    public ResponseEntity<PostResponse> createPost(
            HttpServletRequest httpRequest,
            @Valid @RequestBody PostCreateRequest request) {

        Long userId = (Long) httpRequest.getAttribute("userId");
        PostResponse response = postService.createPost(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 전체 게시글을 조회한다.
    @GetMapping
    public ResponseEntity<List<PostResponse>> getPosts() {
        return ResponseEntity.ok(postService.getPosts());
    }

    // 단건 게시글을 조회한다.
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> getPost(@PathVariable Long id) {
        return ResponseEntity.ok(postService.getPost(id));
    }

    // 게시글을 수정한다.
    @PatchMapping("/{id}")
    public ResponseEntity<PostResponse> updatePost(
            @PathVariable Long id,
            HttpServletRequest httpRequest,
            @Valid @RequestBody PostUpdateRequest request) {

        Long userId = (Long) httpRequest.getAttribute("userId");
        PostResponse response = postService.updatePost(id, userId, request);
        return ResponseEntity.ok(response);
    }

    // 게시글을 삭제한다.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(
            @PathVariable Long id,
            HttpServletRequest httpRequest) {

        Long userId = (Long) httpRequest.getAttribute("userId");
        postService.deletePost(id, userId);
        return ResponseEntity.noContent().build();
    }
}
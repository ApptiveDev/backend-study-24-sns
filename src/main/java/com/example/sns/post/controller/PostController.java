package com.example.sns.post.controller;

import com.example.sns.post.dto.PostCreateRequest;
import com.example.sns.post.dto.PostResponse;
import com.example.sns.post.service.PostService;
import com.example.sns.post.dto.PostUpdateRequest;
import com.example.sns.post.entity.Post;
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

    // 게시물 생성
    @PostMapping
    public ResponseEntity<PostResponse> create(@Valid @RequestBody PostCreateRequest request) {
        PostResponse response = postService.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 전체 조회
    @GetMapping
    public ResponseEntity<List<PostResponse>> findAll() {
        List<PostResponse> response = postService.findAll();
        return ResponseEntity.ok(response);
    }

    // Id별 조회
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> findById(@PathVariable Long id) {
        PostResponse response = postService.findById(id);
        return ResponseEntity.ok(response);
    }

    // 수정
    @PatchMapping("/{id}")
    public ResponseEntity<PostResponse> update(@Valid @PathVariable Long id, @RequestBody PostUpdateRequest request) {
        PostResponse response = postService.update(id, request);
        return ResponseEntity.ok(response);
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<PostResponse> delete(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

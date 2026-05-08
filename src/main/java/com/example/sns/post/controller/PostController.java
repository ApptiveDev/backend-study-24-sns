package com.example.sns.post.controller;

import com.example.sns.post.dto.PostCreateRequest;
import com.example.sns.post.dto.PostResponse;
import com.example.sns.post.service.PostService;
import com.example.sns.post.dto.PostUpdateRequest;
import com.example.sns.post.entity.Post;
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
    public ResponseEntity<PostResponse> create(@RequestBody PostCreateRequest request) {
        Post post = postService.create(request);
        PostResponse response = PostResponse.from(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 전체 조회
    @GetMapping
    public ResponseEntity<List<PostResponse>> findAll() {
        List<Post> posts = postService.findAll();
        List<PostResponse> responses = posts.stream()
                .map(PostResponse::from)
                .toList();
        return ResponseEntity.ok(responses);
    }

    // Id별 조회
    @GetMapping("/{id}")
    public ResponseEntity<PostResponse> findById(@PathVariable Long id) {
        Post post = postService.findById(id);
        PostResponse response = PostResponse.from(post);
        return ResponseEntity.ok(response);
    }

    // 수정
    @PutMapping("/{id}")
    public ResponseEntity<PostResponse> update(@PathVariable Long id, @RequestBody PostUpdateRequest request) {
        Post post = postService.update(id, request);
        PostResponse response = PostResponse.from(post);
        return ResponseEntity.ok(response);
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<PostResponse> delete(@PathVariable Long id) {
        postService.delete(id);
        return ResponseEntity.noContent().build();
    }
}

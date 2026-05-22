package com.example.sns.controller;

import com.example.sns.auth.AuthInterceptor;
import com.example.sns.dto.PostCreateRequest;
import com.example.sns.dto.PostResponse;
import com.example.sns.dto.PostUpdateRequest;
import com.example.sns.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    @PostMapping
    public ResponseEntity<PostResponse> create(
            @RequestAttribute(AuthInterceptor.AUTH_USER_ID) Long authUserId,
            @Valid @RequestBody PostCreateRequest request
    ) {
        PostResponse response = postService.create(authUserId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<Page<PostResponse>> findAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(postService.findAll(page, size));
    }

    @GetMapping("/{postId}")
    public ResponseEntity<PostResponse> findById(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.findById(postId));
    }

    @PatchMapping("/{postId}")
    public ResponseEntity<PostResponse> update(
            @PathVariable Long postId,
            @RequestAttribute(AuthInterceptor.AUTH_USER_ID) Long authUserId,
            @Valid @RequestBody PostUpdateRequest request
    ) {
        return ResponseEntity.ok(postService.update(postId, authUserId, request));
    }

    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> delete(
            @PathVariable Long postId,
            @RequestAttribute(AuthInterceptor.AUTH_USER_ID) Long authUserId
    ) {
        postService.delete(postId, authUserId);
        return ResponseEntity.noContent().build();
    }
}

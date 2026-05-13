package com.example.sns.controller;

import com.example.sns.dto.PostCreateRequest;
import com.example.sns.dto.PostResponse;
import com.example.sns.dto.PostUpdateRequest;
import com.example.sns.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public Long create(@RequestBody PostCreateRequest request) {
        return postService.createPost(request);
    }

    @GetMapping("/{id}")
    public PostResponse getPost(@PathVariable Long id) {
        return postService.findOnePost(id);
    }

    @GetMapping
    public List<PostResponse> getAllPosts() {
        return postService.findAllPost();
    }

    @PatchMapping("/{id}")
    public void update(@PathVariable Long id, @RequestBody PostUpdateRequest request) {
        postService.update(id, request);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        postService.delete(id);
    }


}

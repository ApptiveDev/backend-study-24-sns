package com.example.sns.controller;

import com.example.sns.entity.Post;
import com.example.sns.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public String createPost(@RequestBody Post post) {
        postService.createPost(post);
        return "게시글 등록 완료!";
    }

    @GetMapping
    public List<Post> getPosts() {
        return postService.getPosts();
    }

    @PutMapping("/{id}")
    public String updatePost(@PathVariable Long id, @RequestBody Post post) {
        postService.updatePost(id, post);
        return id + "번 게시글 수정 완료!";
    }

    @DeleteMapping("/{id}")
    public String deletePost(@PathVariable Long id) {
        postService.deletePost(id);
        return id + "번 게시글 삭제 완료!";
    }
}
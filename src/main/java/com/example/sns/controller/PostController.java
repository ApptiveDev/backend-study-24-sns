package com.example.sns.controller;
import org.springframework.web.bind.annotation.*;

import com.example.sns.dto.PostRequestDto;
import com.example.sns.dto.PostResponseDto;
import com.example.sns.service.PostService;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping
    public PostResponseDto createPost(@RequestBody PostRequestDto requestDto){
        return postService.createPost(requestDto);
    }

    @GetMapping
    public List<PostResponseDto> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/{id}")
    public PostResponseDto getPostById(@PathVariable("id") Long id){
        return postService.getPostById(id);
    }

    @PutMapping("/{id}")
    public PostResponseDto updatePost(@PathVariable("id") Long id, @RequestBody PostRequestDto requestDto) {
        return postService.updatePost(id, requestDto);
    }

    @DeleteMapping("/{id}")
    public String deletePost(@PathVariable("id") Long id){
        postService.deletePost(id);

        return "게시글이 성공적으로 삭제되었습니다.";
    }
}

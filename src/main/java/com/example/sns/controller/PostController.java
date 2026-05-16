package com.example.sns.controller;

import com.example.sns.dto.PostCreateRequestDto;
import com.example.sns.dto.PostResponseDto;
import com.example.sns.dto.PostUpdateRequestDto;
import com.example.sns.service.PostService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor // 생성자 코드를 대신 생성.
public class PostController {

    private final PostService postService;

    // 1. 게시글 작성
    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid PostCreateRequestDto requestDto) {
        postService.createPost(requestDto);
        // 새로운 리소스가 생성되었으므로 201 Created를 반환하는 것이 표준
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 2. 전체 조회
    @GetMapping
    public ResponseEntity<List<PostResponseDto>> list() {
        List<PostResponseDto> posts = postService.getAllPosts();
        // 데이터 조회 성공 시 200 OK와 함께 리스트 반환
        return ResponseEntity.ok(postService.getAllPosts());
    }

    // 3. 수정 (일부 수정 허용)
    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid PostUpdateRequestDto requestDto) {
        postService.updatePost(id, requestDto);
        return ResponseEntity.ok().build();
    }

    // 4. 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build(); // 204 전달
    }
}

package com.example.sns.controller;

import com.example.sns.auth.CustomUserDetails;
import com.example.sns.dto.PostCreateRequestDto;
import com.example.sns.dto.PostResponseDto;
import com.example.sns.dto.PostUpdateRequestDto;
import com.example.sns.service.PostService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
@RequiredArgsConstructor // 생성자 코드를 대신 생성.
public class PostController {

    private final PostService postService;

    // 1. 게시글 작성
    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid PostCreateRequestDto requestDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        postService.createPost(requestDto, userDetails.getUserId());
        // 새로운 리소스가 생성되었으므로 201 Created를 반환하는 것이 표준
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // 2. 전체 조회
    @GetMapping
    // GET /api/posts 시 디폴트값 (GET /api/posts?page=0&size=10&sort=createdAt)
    // GET /api/posts?page=1&size=5 가능
    public ResponseEntity<Page<PostResponseDto>> list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sort
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(sort).descending());
        // 데이터 조회 성공 시 200 OK와 함께 리스트 반환
        return ResponseEntity.ok(postService.getAllPosts(pageable));
    }

    // 3. 수정 (일부 수정 허용)
    @PatchMapping("/{id}")
    public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody @Valid PostUpdateRequestDto requestDto, @AuthenticationPrincipal CustomUserDetails userDetails) {
        postService.updatePost(id, requestDto, userDetails.getUserId());
        return ResponseEntity.ok().build();
    }

    // 4. 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, @AuthenticationPrincipal CustomUserDetails userDetails) {
        postService.deletePost(id, userDetails.getUserId());
        return ResponseEntity.noContent().build(); // 204 전달
    }
}

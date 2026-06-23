package com.example.sns.postlike;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class PostLikeController {

    private final PostLikeService postLikeService;

    // Paging 추가
    // 좋아요 토글
    @PostMapping("/posts/{postId}/likes")
    public ResponseEntity<Void> toggleLike(@PathVariable Long postId,
                                           @RequestParam Long userId) {
        postLikeService.toggleLike(postId, userId);
        return ResponseEntity.noContent().build();
    }


    // 좋아요 수 조회
    @GetMapping("/posts/{postId}/likes/count")
    public ResponseEntity<String> countLikes(@PathVariable Long postId) {
        long count = postLikeService.getLikeCount(postId);
        return ResponseEntity.ok("좋아요 수:" + count);
    }
}

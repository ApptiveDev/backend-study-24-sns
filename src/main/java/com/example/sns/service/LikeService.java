package com.example.sns.service;

import com.example.sns.dto.LikeResponseDto;
import com.example.sns.entity.Like;
import com.example.sns.entity.Post;
import com.example.sns.entity.User;
import com.example.sns.repository.LikeRepository;
import com.example.sns.repository.PostRepository;
import com.example.sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    // 좋아요 추가, 취소
    @Transactional
    public void toggleLike(Long postId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));

        Optional<Like> existing = likeRepository.findByUserAndPost(user, post);

        if (existing.isPresent()) {
            likeRepository.delete(existing.get()); // 좋아요 취소
        }
        else {
            likeRepository.save(new Like(user, post)); // 좋아요 추가
        }
    }

    // 좋아요 조회 (유저 수, 유저 목록)
    public LikeResponseDto getLikes(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다."));

        List<Like> likes = likeRepository.findByPost(post);

        List<String> usernames = likes.stream()
                .map(like -> like.getUser().getUsername())
                .toList();

        return new LikeResponseDto(likes.size(), usernames);
    }
}

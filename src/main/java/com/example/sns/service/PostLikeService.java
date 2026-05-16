package com.example.sns.service;

import com.example.sns.entity.Post;
import com.example.sns.entity.PostLike;
import com.example.sns.entity.User;
import com.example.sns.repository.PostLikeRepository;
import com.example.sns.repository.PostRepository;
import com.example.sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public String toggleLike(Long postId, Long userId) {
        Post post = postRepository.findById(postId)
                .orElseThrow (() -> new IllegalArgumentException("게시글이 없습니다."));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저가 없습니다."));

        Optional<PostLike> existingLike = postLikeRepository.findByUserAndPost(user, post);

        if (existingLike.isPresent()) {
            postLikeRepository.delete(existingLike.get());
            return "좋아요가 취소되었습니다.";
        } else {
            PostLike newLike = new PostLike(user, post);
            postLikeRepository.save(newLike);
            return "좋아요가 추가되었습니다.";
        }
    }
}
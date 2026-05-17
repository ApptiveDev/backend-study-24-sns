package com.example.sns.service;

import com.example.sns.entity.Like;
import com.example.sns.exception.PostNotFoundException;
import com.example.sns.repository.LikeRepository;
import com.example.sns.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeQueryService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;

    public long countLikes(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new PostNotFoundException();
        }

        return likeRepository.countByPostId(postId);
    }
}

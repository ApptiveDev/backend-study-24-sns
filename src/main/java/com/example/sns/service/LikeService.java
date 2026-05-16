package com.example.sns.service;

import com.example.sns.entity.Post;
import com.example.sns.entity.PostLike;
import com.example.sns.entity.User;
import com.example.sns.exception.BusinessException;
import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.NotFoundException;
import com.example.sns.repository.LikeRepository;
import com.example.sns.repository.PostRepository;
import com.example.sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LikeService {

    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public void addLike(Long postId, Long userId) {
        Post post = getPost(postId);
        User user = getUser(userId);

        if (likeRepository.existsByPostAndUser(post, user)) {
            throw new BusinessException(ErrorCode.LIKE_ALREADY_EXISTS);
        }

        likeRepository.save(new PostLike(post, user));
    }

    @Transactional
    public void removeLike(Long postId, Long userId) {
        Post post = getPost(postId);
        User user = getUser(userId);

        PostLike postLike = likeRepository.findByPostAndUser(post, user)
                .orElseThrow(() -> new NotFoundException(ErrorCode.LIKE_NOT_FOUND));

        likeRepository.delete(postLike);
    }

    public long countLikes(Long postId) {
        Post post = getPost(postId);
        return likeRepository.countByPost(post);
    }

    private Post getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }
}

package com.example.sns.service;

import com.example.sns.dto.LikeCountResponse;
import com.example.sns.dto.LikeCreateRequest;
import com.example.sns.dto.LikeResponse;
import com.example.sns.entity.Like;
import com.example.sns.entity.Post;
import com.example.sns.entity.User;
import com.example.sns.exception.CustomException;
import com.example.sns.exception.ErrorCode;
import com.example.sns.repository.LikeRepository;
import com.example.sns.repository.PostRepository;
import com.example.sns.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public LikeService(
            LikeRepository likeRepository,
            UserRepository userRepository,
            PostRepository postRepository
    ) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    // 좋아요 누르기
    @Transactional
    public LikeResponse createLike(Long userId, LikeCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Post post = postRepository.findById(request.postId())
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        boolean alreadyLiked = likeRepository.existsByUserIdAndPostId(
                userId,
                request.postId()
        );

        if (alreadyLiked) {
            throw new CustomException(ErrorCode.ALREADY_LIKED);
        }

        Like like = Like.create(user, post);

        Like savedLike = likeRepository.save(like);

        return LikeResponse.from(savedLike);
    }

    // 좋아요 전체 조회
    public List<LikeResponse> getLikes() {
        return likeRepository.findAll()
                .stream()
                .map(LikeResponse::from)
                .toList();
    }

    // 사용자와 게시글 기준으로 좋아요 단건 조회
    public LikeResponse getLikeByUserAndPost(Long userId, Long postId) {
        Like like = likeRepository.findByUserIdAndPostId(userId, postId)
                .orElseThrow(() -> new CustomException(ErrorCode.LIKE_RECORD_NOT_FOUND));

        return LikeResponse.from(like);
    }

    // 특정 게시글 좋아요 목록 조회
    public List<LikeResponse> getLikesByPost(Long postId) {
        postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        return likeRepository.findByPostId(postId)
                .stream()
                .map(LikeResponse::from)
                .toList();
    }

    // 특정 게시글 좋아요 개수 조회
    public LikeCountResponse getLikeCountByPost(Long postId) {
        postRepository.findById(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        long likeCount = likeRepository.countByPostId(postId);

        return new LikeCountResponse(postId, likeCount);
    }

    // 사용자와 게시글 기준으로 좋아요 취소
    @Transactional
    public void deleteLikeByUserAndPost(Long userId, Long postId) {
        Like like = likeRepository.findByUserIdAndPostId(userId, postId)
                .orElseThrow(() -> new CustomException(ErrorCode.LIKE_RECORD_NOT_FOUND));

        likeRepository.delete(like);
    }
}
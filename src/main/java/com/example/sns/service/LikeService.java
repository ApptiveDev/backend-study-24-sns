package com.example.sns.service;

import com.example.sns.dto.LikeCountResponse;
import com.example.sns.dto.LikeCreateRequest;
import com.example.sns.dto.LikeResponse;
import com.example.sns.entity.Like;
import com.example.sns.entity.Post;
import com.example.sns.entity.User;
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
    public LikeResponse createLike(LikeCreateRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Post post = postRepository.findById(request.postId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        boolean alreadyLiked = likeRepository.existsByUserIdAndPostId(
                request.userId(),
                request.postId()
        );

        if (alreadyLiked) {
            throw new IllegalArgumentException("이미 좋아요를 누른 게시글입니다.");
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

    // 좋아요 단건 조회
    public LikeResponse getLike(Long likeId) {
        Like like = likeRepository.findById(likeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 좋아요입니다."));

        return LikeResponse.from(like);
    }

    // 특정 게시글 좋아요 목록 조회
    public List<LikeResponse> getLikesByPost(Long postId) {
        postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        return likeRepository.findByPostId(postId)
                .stream()
                .map(LikeResponse::from)
                .toList();
    }

    // 특정 게시글 좋아요 개수 조회
    public LikeCountResponse getLikeCountByPost(Long postId) {
        postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        long likeCount = likeRepository.countByPostId(postId);

        return new LikeCountResponse(postId, likeCount);
    }

    // 좋아요 취소
    @Transactional
    public void deleteLike(Long likeId) {
        Like like = likeRepository.findById(likeId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 좋아요입니다."));

        likeRepository.delete(like);
    }

    // 사용자와 게시글 기준으로 좋아요 취소
    @Transactional
    public void deleteLikeByUserAndPost(Long userId, Long postId) {
        Like like = likeRepository.findByUserIdAndPostId(userId, postId)
                .orElseThrow(() -> new IllegalArgumentException("좋아요를 누른 기록이 없습니다."));

        likeRepository.delete(like);
    }
}
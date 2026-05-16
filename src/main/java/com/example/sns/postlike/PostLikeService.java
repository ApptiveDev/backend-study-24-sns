package com.example.sns.postlike;

import com.example.sns.exception.PostNotFoundException;
import com.example.sns.exception.UserNotFoundException;
import com.example.sns.post.entity.Post;
import com.example.sns.post.repository.PostRepository;
import com.example.sns.user.entity.User;
import com.example.sns.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostLikeService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostLikeRepository postLikeRepository;

    // 좋아요 토글
    @Transactional
    public void toggleLike(Long postId, Long userId) {
        if (postLikeRepository.existsByUserIdAndPostId(userId, postId)) {
            // 이미 좋아요 -> 취소
            postLikeRepository.deleteByUserIdAndPostId(userId, postId);
        } else {

            User user = userRepository.findById(userId).orElseThrow(
                    () -> new UserNotFoundException(userId)
            );

            Post post = postRepository.findById(postId).orElseThrow(
                    () -> new PostNotFoundException(postId));

            PostLike postLike = new PostLike(user, post);

            postLikeRepository.save(postLike);
        }
    }


    // 좋아요 수 조회
    public long getLikeCount(Long postId) {
        return postLikeRepository.countByPostId(postId);
    }


    // 내가 좋아요 눌렀는지 확인
    public boolean isLiked(Long userId, Long postId) {
        return postLikeRepository.existsByUserIdAndPostId(userId, postId);
    }
}

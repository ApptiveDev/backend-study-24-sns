package com.example.sns.service;

import com.example.sns.entity.Like;
import com.example.sns.entity.Post;
import com.example.sns.entity.User;
import com.example.sns.exception.AlreadyLikedException;
import com.example.sns.exception.LikeNotFoundException;
import com.example.sns.exception.PostNotFoundException;
import com.example.sns.exception.UserNotFoundException;
import com.example.sns.repository.LikeRepository;
import com.example.sns.repository.PostRepository;
import com.example.sns.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class LikeCommandService {
    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public void likePost(Long postId, Long userId) {
        if (likeRepository.existsByUserIdAndPostId(userId, postId)) {
            throw new AlreadyLikedException();
        }

        User user = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Post post = postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);

        Like like = Like.create(user, post);

        likeRepository.save(like);
    }

    public void unlikePost(Long postId, Long userId) {
        Like like = likeRepository.findByUserIdAndPostId(userId, postId)
                .orElseThrow(LikeNotFoundException::new);
    }
}

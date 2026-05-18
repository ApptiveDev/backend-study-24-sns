package com.example.sns.service;

import org.springframework.stereotype.Service;

import com.example.sns.dto.LikesRequestDto;
import com.example.sns.dto.LikesResponseDto;
import com.example.sns.entity.Likes;
import com.example.sns.entity.Post;
import com.example.sns.entity.User;
import com.example.sns.repository.LikesRepository;
import com.example.sns.repository.PostRepository;
import com.example.sns.repository.UserRepository;

@Service
public class LikesService {
    private final LikesRepository likesRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public LikesService(LikesRepository likesRepository, PostRepository postRepository, UserRepository userRepository){
        this.likesRepository = likesRepository;
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    public LikesResponseDto toggleLike(Long postId, LikesRequestDto requestDto){
        Post post = postRepository.findById(postId)
            .orElseThrow(() -> new IllegalArgumentException("유효한 게시물이 아닙니다."));

        User user = userRepository.findById(requestDto.userId())
            .orElseThrow(() -> new IllegalArgumentException("유효한 사용자가 아닙니다."));

        Likes existingLike = likesRepository.findByPostIdAndUserId(postId, requestDto.userId());

        if(existingLike != null){
            likesRepository.delete(existingLike);
            return new LikesResponseDto("좋아요를 취소했습니다.");
        }else{
            Likes newLike = new Likes();
            newLike.setPost(post);
            newLike.setUser(user);
            likesRepository.save(newLike);
            return new LikesResponseDto("좋아요를 눌렀습니다.");
        }
    }

    public int getLikesCount(Long postId){
        return likesRepository.countByPostId(postId);
    }
}

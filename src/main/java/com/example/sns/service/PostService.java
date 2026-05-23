package com.example.sns.service;

import com.example.sns.dto.PostCreateRequest;
import com.example.sns.dto.PostResponse;
import com.example.sns.dto.PostUpdateRequest;
import com.example.sns.entity.Post;
import com.example.sns.entity.User;
import com.example.sns.exception.ForbiddenException;
import com.example.sns.exception.PostNotFoundException;
import com.example.sns.exception.UserNotFoundException;
import com.example.sns.repository.PostRepository;
import com.example.sns.repository.UserRepository;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public Long createPost(Long userId, PostCreateRequest request) {
        User author = userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);

        Post post = Post.create(author, request.title(), request.content());

        return postRepository.save(post).getId();
    }

    @Transactional
    public void update(Long postId, Long userId, PostUpdateRequest request) {
        Post post = findPost(postId);
        User requester = findUser(userId);

        if (!post.isWrittenBy(requester)) {
            throw new ForbiddenException("게시글 작성자만 수정할 수 있습니다.");
        }

        post.update(request.title(), request.content());
    }

    @Transactional
    public void delete(Long postId, Long userId) {
        Post post = findPost(postId);
        User requester = findUser(userId);

        if (!post.isWrittenBy(requester)) {
            throw new ForbiddenException("게시글 작성자만 삭제할 수 있습니다.");
        }

        postRepository.delete(post);
    }

    public List<PostResponse> findAllPost() {
        return postRepository.findAllWithAuthor().stream()
                .map(PostResponse::from)
                .toList();
    }

    private Post findPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(PostNotFoundException::new);
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }
}

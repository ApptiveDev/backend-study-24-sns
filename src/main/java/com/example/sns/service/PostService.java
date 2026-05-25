package com.example.sns.service;

import com.example.sns.dto.PostCreateRequest;
import com.example.sns.dto.PostResponse;
import com.example.sns.dto.PostUpdateRequest;
import com.example.sns.entity.Post;
import com.example.sns.entity.User;
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
    public Long createPost(PostCreateRequest request) {
        User author = userRepository.findById(request.authorId())
                .orElseThrow(UserNotFoundException::new);

        Post post = Post.create(author, request.title(), request.content());

        return postRepository.save(post).getId();
    }

    @Transactional
    public void update(Long postId, PostUpdateRequest request) {
        Post post = findPost(postId);

        post.update(request.title(), request.content());
    }

    @Transactional
    public void delete(Long postId) {
        Post post = findPost(postId);

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
}

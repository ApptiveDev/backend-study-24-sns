package com.example.sns.service;

import com.example.sns.dto.PostCreateRequest;
import com.example.sns.dto.PostResponse;
import com.example.sns.dto.PostUpdateRequest;
import com.example.sns.entity.Post;
import com.example.sns.entity.User;
import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.ForbiddenException;
import com.example.sns.exception.NotFoundException;
import com.example.sns.repository.PostRepository;
import com.example.sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public PostResponse create(Long authUserId, PostCreateRequest request) {
        User user = getUser(authUserId);

        Post post = new Post(user, request.getTitle(), request.getContent());
        Post savedPost = postRepository.save(post);

        return PostResponse.from(savedPost);
    }

    public Page<PostResponse> findAll(int page, int size) {
        PageRequest pageRequest = PageRequest.of(
                page,
                size,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        return postRepository.findAll(pageRequest)
                .map(PostResponse::from);
    }

    public PostResponse findById(Long postId) {
        Post post = getPost(postId);
        return PostResponse.from(post);
    }

    @Transactional
    public PostResponse update(Long postId, Long authUserId, PostUpdateRequest request) {
        Post post = getPost(postId);
        validateOwner(post, authUserId);
        post.update(request.getTitle(), request.getContent());

        return PostResponse.from(post);
    }

    @Transactional
    public void delete(Long postId, Long authUserId) {
        Post post = getPost(postId);
        validateOwner(post, authUserId);
        postRepository.delete(post);
    }

    private Post getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    private void validateOwner(Post post, Long authUserId) {
        if (!post.getUser().getId().equals(authUserId)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN);
        }
    }
}

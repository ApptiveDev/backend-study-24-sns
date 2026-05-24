package com.example.sns.service;

import com.example.sns.dto.PostCreateRequest;
import com.example.sns.dto.PostResponse;
import com.example.sns.dto.PostUpdateRequest;
import com.example.sns.entity.Post;
import com.example.sns.entity.User;
import com.example.sns.exception.CustomException;
import com.example.sns.exception.ErrorCode;
import com.example.sns.repository.PostRepository;
import com.example.sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 게시글 생성을 처리한다.
    @Transactional
    public PostResponse createPost(Long userId, PostCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Post post = new Post(request.title(), request.content(), user);
        Post savedPost = postRepository.save(post);
        return PostResponse.from(savedPost);
    }

    // 전체 게시글 조회를 처리한다.
    public List<PostResponse> getPosts() {
        return postRepository.findAllWithUser().stream()
                .map(PostResponse::from)
                .collect(Collectors.toList());
    }

    // 단건 게시글 조회를 처리한다.
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        return PostResponse.from(post);
    }

    // 게시글 수정 로직을 엔티티에 위임하여 처리한다.
    @Transactional
    public PostResponse updatePost(Long id, Long userId, PostUpdateRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        post.update(request.title(), request.content(), requester);
        return PostResponse.from(post);
    }

    // 작성자 권한을 확인하고 게시글을 삭제한다.
    @Transactional
    public void deletePost(Long id, Long userId) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));

        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!post.isWrittenBy(requester)) {
            throw new CustomException(ErrorCode.NO_PERMISSION);
        }

        postRepository.delete(post);
    }
}
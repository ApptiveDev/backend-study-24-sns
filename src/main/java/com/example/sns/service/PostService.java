package com.example.sns.service;

import com.example.sns.dto.PostCreateRequest;
import com.example.sns.dto.PostResponse;
import com.example.sns.dto.PostUpdateRequest;
import com.example.sns.entity.Post;
import com.example.sns.entity.User;
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

    @Transactional
    public PostResponse createPost(PostCreateRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다."));

        // Setter 대신 생성자로 안전하게 조립
        Post post = new Post(request.title(), request.content(), user);

        Post savedPost = postRepository.save(post);
        return PostResponse.from(savedPost);
    }

    public List<PostResponse> getPosts() {
        // N+1 문제 해결된 메서드 호출
        return postRepository.findAllWithUser().stream()
                .map(PostResponse::from)
                .collect(Collectors.toList());
    }

    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        return PostResponse.from(post);
    }

    @Transactional
    public PostResponse updatePost(Long id, PostUpdateRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        // Setter 대신 update 메서드 활용
        post.update(request.title(), request.content());

        return PostResponse.from(post);
    }

    @Transactional
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
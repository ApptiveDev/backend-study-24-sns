package com.example.sns.post.service;

import com.example.sns.post.dto.PostCreateRequest;
import com.example.sns.post.repository.PostRepository;
import com.example.sns.post.dto.PostUpdateRequest;
import com.example.sns.post.entity.Post;
import com.example.sns.user.entity.User;
import com.example.sns.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public Post create(PostCreateRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Post post = new Post(request.title(), request.content(), user);
        return postRepository.save(post);
    }

    // 전체 조회
    public List<Post> findAll() {
        return postRepository.findAll();
    }

    // Id별 조회
    public Post findById(Long id) {
        return postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));
    }

    // 수정
    public Post update(Long id, PostUpdateRequest request) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));

        post.update(request.title(), request.content());
        return postRepository.save(post);
    }

    // 삭제
    public void delete(Long id) {
        postRepository.deleteById(id);
    }
}

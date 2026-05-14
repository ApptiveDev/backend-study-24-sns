package com.example.sns.post.service;

import com.example.sns.post.dto.PostCreateRequest;
import com.example.sns.post.dto.PostResponse;
import com.example.sns.post.repository.PostRepository;
import com.example.sns.post.dto.PostUpdateRequest;
import com.example.sns.post.entity.Post;
import com.example.sns.user.entity.User;
import com.example.sns.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public PostResponse create(PostCreateRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Post post = new Post(request.title(), request.content(), user);
        postRepository.save(post);

        System.out.println(post.getId());
        return PostResponse.from(post);
    }

    // 전체 조회
    public List<PostResponse> findAll() {
        return postRepository.findAll()
                .stream()
                .map(PostResponse::from)
                .toList();
    }

    // Id별 조회
    public PostResponse findById(Long id) {
            Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));

            return PostResponse.from(post);
    }

    // 수정
    @Transactional
    public PostResponse update(Long id, PostUpdateRequest request) {
        Post post = postRepository.findById(id).orElseThrow(
                () -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));

        post.update(request.title(), request.content());

        //  postRepository.save(post);

        return PostResponse.from(post);
    }

    // 삭제
    @Transactional
    public void delete(Long id) {
        postRepository.deleteById(id);
    }

}

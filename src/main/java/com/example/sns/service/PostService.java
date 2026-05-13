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
@Transactional(readOnly = true) // 전체적으로 읽기 전용으로 두고, 수정/삭제에만 쓰기 권한을 줍니다.
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository; // 유저 정보를 찾기 위해 추가!

    // 1. 게시글 생성 (DTO를 받아서 -> Entity로 만들고 저장 -> 다시 DTO로 반환)
    @Transactional
    public PostResponse createPost(PostCreateRequest request) {
        // userId로 진짜 유저 객체를 찾습니다.
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new IllegalArgumentException("게시글을 작성할 유저를 찾을 수 없습니다."));

        Post post = new Post();
        post.setTitle(request.title()); // record DTO의 값을 꺼냅니다.
        post.setContent(request.content());
        post.setUser(user); // 객체를 통째로 연결합니다 (@ManyToOne)

        Post savedPost = postRepository.save(post);
        return PostResponse.from(savedPost); // 저장된 결과를 예쁘게 포장해서 돌려줍니다.
    }

    // 2. 게시글 전체 조회
    public List<PostResponse> getPosts() {
        return postRepository.findAll().stream()
                .map(PostResponse::from) // List 안의 Post들을 전부 PostResponse로 변환!
                .collect(Collectors.toList());
    }

    // 3. 단건 조회 (기존 코드에 없어서 컨트롤러에서 에러가 났던 부분 추가!)
    public PostResponse getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        return PostResponse.from(post);
    }

    // 4. 게시글 수정
    @Transactional
    public PostResponse updatePost(Long id, PostUpdateRequest request) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));

        post.setTitle(request.title());
        post.setContent(request.content());

        return PostResponse.from(post);
    }

    // 5. 게시글 삭제
    @Transactional
    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
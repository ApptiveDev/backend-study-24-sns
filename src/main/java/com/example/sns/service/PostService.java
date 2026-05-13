package com.example.sns.service; // 이 파일의 위치

import com.example.sns.dto.PostCreateRequestDto;
import com.example.sns.dto.PostResponseDto;
import com.example.sns.dto.PostUpdateRequestDto;
import com.example.sns.entity.Post;
import com.example.sns.entity.User;
import com.example.sns.repository.PostRepository;
import com.example.sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // @Transactional 어노테이션

import java.util.List; // List 타입 사용

// 이 클래스가 Service임을 선언하는 어노테이션
@Service
@Transactional // 데이터 변경 시 필요
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    // 작성
    public void createPost(PostCreateRequestDto dto) {
        // 숫자로 된 ID를 가지고 DB에서 진짜 User 객체를 찾아옴.
        User user = userRepository.findById(dto.userId())
                .orElseThrow(() -> new IllegalArgumentException("해당 유저를 찾을 수 없습니다."));

        Post post = new Post(dto.title(), dto.content(), user);
        postRepository.save(post);
    }

    // 전체 조회
    @Transactional(readOnly = true) // 수정 불가 오버라이드
    public List<PostResponseDto> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        return posts.stream()
                .map(post -> new PostResponseDto(post.getId(), post.getTitle(), post.getContent(), post.getUser().getUsername(), post.getCreatedAt(), post.getUpdatedAt()))
                .toList();
    }

    // 3. 수정
    public void updatePost(Long id, PostUpdateRequestDto dto) {
        // DB에서 수정할 글을 먼저 찾아옴.
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));

        // 엔티티의 데이터를 바꿈. (Transactional 덕분에 자동으로 DB에 반영됨)
        // 값이 들어온 경우에만 각각의 수정 함수를 호출 (PATCH의 핵심)
        if (dto.title() != null) {
            post.updateTitle(dto.title());
        }
        if (dto.content() != null) {
            post.updateContent(dto.content());
        }
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}

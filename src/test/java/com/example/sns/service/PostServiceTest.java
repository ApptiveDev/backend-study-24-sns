package com.example.sns.service;

import com.example.sns.dto.PostCreateRequest;
import com.example.sns.dto.PostResponse;
import com.example.sns.dto.PostUpdateRequest;
import com.example.sns.entity.Post;
import com.example.sns.entity.User;
import com.example.sns.repository.PostRepository;
import com.example.sns.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {

    @Mock
    private PostRepository postRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PostService postService;

    @Test
    @DisplayName("게시글 작성에 성공한다")
    void createPost_success() {
        // Given
        User user = User.create("test@test.com", "테스트유저");
        ReflectionTestUtils.setField(user, "id", 1L);

        PostCreateRequest request = new PostCreateRequest(
                1L,
                "첫 번째 게시글",
                "게시글 내용입니다."
        );

        given(userRepository.findById(1L))
                .willReturn(Optional.of(user));

        given(postRepository.save(any(Post.class)))
                .willAnswer(invocation -> {
                    Post post = invocation.getArgument(0);
                    ReflectionTestUtils.setField(post, "id", 1L);
                    return post;
                });

        // When
        PostResponse response = postService.createPost(request);

        // Then
        assertAll(
                () -> assertEquals(1L, response.postId()),
                () -> assertEquals("첫 번째 게시글", response.title()),
                () -> assertEquals("게시글 내용입니다.", response.content()),
                () -> assertEquals(1L, response.writerId()),
                () -> assertEquals("테스트유저", response.writerNickname())
        );

        verify(userRepository, times(1)).findById(1L);
        verify(postRepository, times(1)).save(any(Post.class));
    }

    @Test
    @DisplayName("존재하지 않는 사용자로 게시글 작성 시 예외가 발생한다")
    void createPost_userNotFound() {
        // Given
        PostCreateRequest request = new PostCreateRequest(
                999L,
                "제목",
                "내용"
        );

        given(userRepository.findById(999L))
                .willReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> postService.createPost(request)
        );

        assertEquals("존재하지 않는 사용자입니다.", exception.getMessage());

        verify(userRepository, times(1)).findById(999L);
        verify(postRepository, never()).save(any(Post.class));
    }

    @Test
    @DisplayName("게시글 전체 조회에 성공한다")
    void getPosts_success() {
        // Given
        User user = User.create("test@test.com", "테스트유저");
        ReflectionTestUtils.setField(user, "id", 1L);

        Post post1 = Post.create("첫 번째 게시글", "내용1", user);
        ReflectionTestUtils.setField(post1, "id", 1L);

        Post post2 = Post.create("두 번째 게시글", "내용2", user);
        ReflectionTestUtils.setField(post2, "id", 2L);

        given(postRepository.findAll())
                .willReturn(List.of(post1, post2));

        // When
        List<PostResponse> responses = postService.getPosts();

        // Then
        assertEquals(2, responses.size());
        assertEquals("첫 번째 게시글", responses.get(0).title());
        assertEquals("두 번째 게시글", responses.get(1).title());

        verify(postRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("게시글 단건 조회에 성공한다")
    void getPost_success() {
        // Given
        User user = User.create("test@test.com", "테스트유저");
        ReflectionTestUtils.setField(user, "id", 1L);

        Post post = Post.create("게시글 제목", "게시글 내용", user);
        ReflectionTestUtils.setField(post, "id", 1L);

        given(postRepository.findById(1L))
                .willReturn(Optional.of(post));

        // When
        PostResponse response = postService.getPost(1L);

        // Then
        assertAll(
                () -> assertEquals(1L, response.postId()),
                () -> assertEquals("게시글 제목", response.title()),
                () -> assertEquals("게시글 내용", response.content())
        );

        verify(postRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("존재하지 않는 게시글 조회 시 예외가 발생한다")
    void getPost_postNotFound() {
        // Given
        given(postRepository.findById(999L))
                .willReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> postService.getPost(999L)
        );

        assertEquals("존재하지 않는 게시글입니다.", exception.getMessage());

        verify(postRepository, times(1)).findById(999L);
    }

    @Test
    @DisplayName("게시글 수정에 성공한다")
    void updatePost_success() {
        // Given
        User user = User.create("test@test.com", "테스트유저");
        ReflectionTestUtils.setField(user, "id", 1L);

        Post post = Post.create("기존 제목", "기존 내용", user);
        ReflectionTestUtils.setField(post, "id", 1L);

        PostUpdateRequest request = new PostUpdateRequest(
                "수정된 제목",
                "수정된 내용"
        );

        given(postRepository.findById(1L))
                .willReturn(Optional.of(post));

        // When
        PostResponse response = postService.updatePost(1L, request);

        // Then
        assertEquals("수정된 제목", response.title());
        assertEquals("수정된 내용", response.content());

        verify(postRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("게시글 삭제에 성공한다")
    void deletePost_success() {
        // Given
        User user = User.create("test@test.com", "테스트유저");
        ReflectionTestUtils.setField(user, "id", 1L);

        Post post = Post.create("삭제할 게시글", "내용", user);
        ReflectionTestUtils.setField(post, "id", 1L);

        given(postRepository.findById(1L))
                .willReturn(Optional.of(post));

        // When
        postService.deletePost(1L);

        // Then
        verify(postRepository, times(1)).findById(1L);
        verify(postRepository, times(1)).delete(post);
    }
}
package com.example.sns.comment.service;

import com.example.sns.comment.dto.CommentCreateRequest;
import com.example.sns.comment.dto.CommentResponse;
import com.example.sns.comment.dto.CommentUpdateRequest;
import com.example.sns.comment.entity.Comment;
import com.example.sns.comment.repository.CommentRepository;
import com.example.sns.exception.CommentNotFoundException;
import com.example.sns.exception.PostNotFoundException;
import com.example.sns.exception.UserNotFoundException;
import com.example.sns.post.entity.Post;
import com.example.sns.post.repository.PostRepository;
import com.example.sns.user.entity.User;
import com.example.sns.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

/**
 * CommentService 단위 테스트
 */
@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    private CommentRepository commentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PostRepository postRepository;

    @InjectMocks
    private CommentService commentService;

    @Test
    @DisplayName("댓글 생성 성공")
    void createComment_Success() {
        // Given
        Long postId = 1L;
        Long userId = 1L;
        String content = "댓글 내용입니다";

        User user = new User("test@test.com", "testuser", "password123");
        Post post = Post.createPost("게시글 제목", "게시글 내용", user);
        Comment comment = new Comment(content, user, post);

        CommentCreateRequest request = new CommentCreateRequest(userId, content);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(postRepository.findById(postId)).willReturn(Optional.of(post));
        given(commentRepository.save(any(Comment.class))).willReturn(comment);

        // When
        CommentResponse response = commentService.create(postId, request);

        // Then
        assertThat(response).isNotNull();
        assertThat(response.content()).isEqualTo(content);

        verify(userRepository).findById(userId);
        verify(postRepository).findById(postId);
        verify(commentRepository).save(any(Comment.class));
    }

    @Test
    @DisplayName("존재하지 않는 사용자로 댓글 생성 시 예외 발생")
    void createComment_UserNotFound() {
        // Given
        Long postId = 1L;
        Long userId = 999L;
        CommentCreateRequest request = new CommentCreateRequest(userId, "댓글 내용");

        given(userRepository.findById(userId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> commentService.create(postId, request))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("사용자를 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("존재하지 않는 게시글에 댓글 생성 시 예외 발생")
    void createComment_PostNotFound() {
        // Given
        Long postId = 999L;
        Long userId = 1L;
        User user = new User("test@test.com", "testuser", "password123");
        CommentCreateRequest request = new CommentCreateRequest(userId, "댓글 내용");

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(postRepository.findById(postId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> commentService.create(postId, request))
                .isInstanceOf(PostNotFoundException.class)
                .hasMessageContaining("게시물을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("댓글 수정 성공")
    void updateComment_Success() {
        // Given
        Long commentId = 1L;
        Long userId = 1L;
        String newContent = "수정된 댓글 내용";

        User user = new User("test@test.com", "testuser", "password123");
        Post post = Post.createPost("게시글 제목", "게시글 내용", user);
        Comment comment = new Comment("원본 댓글 내용", user, post);

        CommentUpdateRequest request = new CommentUpdateRequest(userId, newContent);

        given(commentRepository.findById(commentId)).willReturn(Optional.of(comment));

        // When
        CommentResponse response = commentService.update(commentId, request);

        // Then
        assertThat(response.content()).isEqualTo(newContent);
        verify(commentRepository).findById(commentId);
    }

    @Test
    @DisplayName("존재하지 않는 댓글 수정 시 예외 발생")
    void updateComment_NotFound() {
        // Given
        Long commentId = 999L;
        Long userId = 1L;
        CommentUpdateRequest request = new CommentUpdateRequest(userId, "수정된 내용");

        given(commentRepository.findById(commentId)).willReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> commentService.update(commentId, request))
                .isInstanceOf(CommentNotFoundException.class)
                .hasMessageContaining("댓글을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("댓글 삭제 성공")
    void deleteComment_Success() {
        // Given
        Long commentId = 1L;

        // When
        commentService.delete(commentId);

        // Then
        verify(commentRepository).deleteById(commentId);
    }
}
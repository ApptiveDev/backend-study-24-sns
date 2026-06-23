package com.example.sns.comment.controller;

import com.example.sns.comment.dto.CommentUpdateRequest;
import com.example.sns.comment.entity.Comment;
import com.example.sns.comment.repository.CommentRepository;
import com.example.sns.post.entity.Post;
import com.example.sns.post.repository.PostRepository;
import com.example.sns.user.entity.User;
import com.example.sns.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * CommentController 통합 테스트
 *
 * 전역 예외 핸들러가 적용되어 있으므로:
 * - 존재하지 않는 리소스 → 404 Not Found (예전: 500 에러)
 * - 에러 메시지가 JSON으로 반환됨
 */
@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostRepository postRepository;

    private User user;
    private Post post;
    private Comment comment;

    @BeforeEach
    void setUp() {
        user = new User("test@test.com", "testuser", "password123");
        userRepository.save(user);

        post = Post.createPost("테스트 게시글", "게시글 내용", user);
        postRepository.save(post);

        comment = new Comment("원본 댓글", user, post);
        commentRepository.save(comment);
    }

    @Test
    @DisplayName("댓글 수정 API 성공")
    void updateComment_Success() throws Exception {
        // Given
        String newContent = "수정된 댓글 내용";
        CommentUpdateRequest request = new CommentUpdateRequest(user.getId(), newContent);

        // When & Then
        mockMvc.perform(patch("/comments/{commentId}", comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").value(newContent))
                .andExpect(jsonPath("$.userName").value(user.getName()));
    }

    @Test
    @DisplayName("존재하지 않는 댓글 수정 시 404 에러")
    void updateComment_NotFound() throws Exception {
        // Given
        Long invalidCommentId = 999L;
        CommentUpdateRequest request = new CommentUpdateRequest(user.getId(), "수정 내용");

        // When & Then
        // 전역 핸들러가 처리 → 404 Not Found + 에러 메시지 JSON
        mockMvc.perform(patch("/comments/{commentId}", invalidCommentId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isNotFound())  // 404
                .andExpect(jsonPath("$.message").value("댓글을 찾을 수 없습니다. (ID: " + invalidCommentId + ")"));
    }

    @Test
    @DisplayName("댓글 삭제 API 성공")
    void deleteComment_Success() throws Exception {
        // When
        mockMvc.perform(delete("/comments/{commentId}", comment.getId()))
                .andDo(print())
                .andExpect(status().isOk());

        // Then: DB에서 실제로 삭제되었는지 확인
        assertThat(commentRepository.findById(comment.getId())).isEmpty();
    }
}
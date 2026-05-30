package com.example.sns.service;

import com.example.sns.dto.CommentCreateRequestDto;
import com.example.sns.dto.CommentResponseDto;
import com.example.sns.dto.CommentUpdateRequestDto;
import com.example.sns.entity.Comment;
import com.example.sns.entity.Post;
import com.example.sns.entity.User;
import com.example.sns.exception.CommentNotFoundException;
import com.example.sns.exception.PostNotFoundException;
import com.example.sns.exception.UserNotFoundException;
import com.example.sns.repository.CommentRepository;
import com.example.sns.repository.PostRepository;
import com.example.sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true) // 기본적으로 읽기 전용으로 설정 (성능 최적화)
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    // 댓글 작성
    @Transactional // 쓰기 권한이 필요함
    public void createComment(Long postId, CommentCreateRequestDto dto, Long userId) {
        // userId로 DB에서 User 객체를 찾아옴
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        // postId로 DB에서 Post 객체를 찾아옴
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
        // Comment 객체를 만들어서 저장
        commentRepository.save(new Comment(dto.content(), user, post));
    }

    // 댓글 조회
    public List<CommentResponseDto> getComments(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));
        // Entity -> Dto 변환하여 리턴
        return commentRepository.findByPostWithUser(post).stream()
                .map(comment -> new CommentResponseDto(
                        comment.getId(),
                        comment.getContent(),
                        comment.getUser().getUsername(),
                        comment.getCreatedAt(),
                        comment.getUpdatedAt()
                ))
                .toList();
    }

    // 댓글 수정
    @Transactional // 쓰기 권한이 필요함
    public void updateComment(Long commentId, CommentUpdateRequestDto dto) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));
        // 엔티티가 값 검증하고 업데이트함.
        comment.update(dto.content());
    }

    // 댓글 삭제
    @Transactional // 쓰기 권한이 필요함
    public void deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}

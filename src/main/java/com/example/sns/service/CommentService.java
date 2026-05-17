package com.example.sns.service;

import com.example.sns.dto.CommentCreateRequest;
import com.example.sns.dto.CommentResponse;
import com.example.sns.dto.CommentUpdateRequest;
import com.example.sns.entity.Comment;
import com.example.sns.entity.Post;
import com.example.sns.entity.User;
import com.example.sns.repository.CommentRepository;
import com.example.sns.repository.PostRepository;
import com.example.sns.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    public CommentService(
            CommentRepository commentRepository,
            UserRepository userRepository,
            PostRepository postRepository
    ) {
        this.commentRepository = commentRepository;
        this.userRepository = userRepository;
        this.postRepository = postRepository;
    }

    // 댓글 작성
    @Transactional
    public CommentResponse createComment(CommentCreateRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 사용자입니다."));

        Post post = postRepository.findById(request.postId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        Comment comment = Comment.create(
                request.content(),
                user,
                post
        );

        Comment savedComment = commentRepository.save(comment);

        return CommentResponse.from(savedComment);
    }

    // 댓글 전체 조회
    public List<CommentResponse> getComments() {
        return commentRepository.findAll()
                .stream()
                .map(CommentResponse::from)
                .toList();
    }

    // 특정 게시글의 댓글 조회
    public List<CommentResponse> getCommentsByPost(Long postId) {
        postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 게시글입니다."));

        return commentRepository.findByPostId(postId)
                .stream()
                .map(CommentResponse::from)
                .toList();
    }

    // 댓글 단건 조회
    public CommentResponse getComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        return CommentResponse.from(comment);
    }

    // 댓글 수정
    @Transactional
    public CommentResponse updateComment(Long commentId, CommentUpdateRequest request) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        comment.update(request.content());

        return CommentResponse.from(comment);
    }

    // 댓글 삭제
    @Transactional
    public void deleteComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 댓글입니다."));

        commentRepository.delete(comment);
    }
}
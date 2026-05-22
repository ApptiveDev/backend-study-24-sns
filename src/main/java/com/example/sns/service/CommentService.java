package com.example.sns.service;

import com.example.sns.dto.CommentCreateRequest;
import com.example.sns.dto.CommentResponse;
import com.example.sns.dto.CommentUpdateRequest;
import com.example.sns.entity.Comment;
import com.example.sns.entity.Post;
import com.example.sns.entity.User;
import com.example.sns.exception.ErrorCode;
import com.example.sns.exception.ForbiddenException;
import com.example.sns.exception.NotFoundException;
import com.example.sns.repository.CommentRepository;
import com.example.sns.repository.PostRepository;
import com.example.sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentResponse create(Long authUserId, CommentCreateRequest request) {
        Post post = getPost(request.getPostId());
        User user = getUser(authUserId);

        Comment comment = new Comment(post, user, request.getContent());
        Comment savedComment = commentRepository.save(comment);

        return CommentResponse.from(savedComment);
    }

    public List<CommentResponse> findAllByPostId(Long postId) {
        Post post = getPost(postId);
        return commentRepository.findAllByPostOrderByCreatedAtAsc(post).stream()
                .map(CommentResponse::from)
                .toList();
    }

    public CommentResponse findById(Long commentId) {
        Comment comment = getComment(commentId);
        return CommentResponse.from(comment);
    }

    @Transactional
    public CommentResponse update(Long commentId, Long authUserId, CommentUpdateRequest request) {
        Comment comment = getComment(commentId);
        validateOwner(comment, authUserId);
        comment.update(request.getContent());
        return CommentResponse.from(comment);
    }

    @Transactional
    public void delete(Long commentId, Long authUserId) {
        Comment comment = getComment(commentId);
        validateOwner(comment, authUserId);
        commentRepository.delete(comment);
    }

    private Comment getComment(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.COMMENT_NOT_FOUND));
    }

    private Post getPost(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.POST_NOT_FOUND));
    }

    private User getUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.USER_NOT_FOUND));
    }

    private void validateOwner(Comment comment, Long authUserId) {
        if (!comment.getUser().getId().equals(authUserId)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN);
        }
    }
}

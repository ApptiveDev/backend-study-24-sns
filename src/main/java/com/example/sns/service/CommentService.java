package com.example.sns.service;

import com.example.sns.dto.CommentRequest;
import com.example.sns.dto.CommentResponse;
import com.example.sns.entity.Comment;
import com.example.sns.entity.Post;
import com.example.sns.entity.User;
import com.example.sns.exception.CustomException;
import com.example.sns.exception.ErrorCode;
import com.example.sns.repository.CommentRepository;
import com.example.sns.repository.PostRepository;
import com.example.sns.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentResponse createComment(Long postId, Long userId, CommentRequest request) {
        Post post = postRepository.findByIdWithUser(postId)
                .orElseThrow(() -> new CustomException(ErrorCode.POST_NOT_FOUND));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        Comment comment = new Comment(request.content(), user, post);
        Comment savedComment = commentRepository.save(comment);

        return CommentResponse.from(
                commentRepository.findByIdAndPostId(savedComment.getId(), postId)
                        .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND))
        );
    }

    @Transactional
    public CommentResponse updateComment(Long postId, Long commentId, Long userId, CommentRequest request) {
        // postId + commentId로 조회 → 소속 검증이 자동으로 됨
        Comment comment = commentRepository.findByIdAndPostId(commentId, postId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        comment.update(request.content(), requester);
        return CommentResponse.from(comment);
    }

    @Transactional
    public void deleteComment(Long postId, Long commentId, Long userId) {
        // postId + commentId로 조회 → 소속 검증이 자동으로 됨
        Comment comment = commentRepository.findByIdAndPostId(commentId, postId)
                .orElseThrow(() -> new CustomException(ErrorCode.COMMENT_NOT_FOUND));

        User requester = userRepository.findById(userId)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));

        if (!comment.isWrittenBy(requester)) {
            throw new CustomException(ErrorCode.NO_PERMISSION);
        }

        commentRepository.delete(comment);
    }
}
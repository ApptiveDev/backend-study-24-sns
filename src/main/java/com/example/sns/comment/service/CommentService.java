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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository commentRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    // 댓글 생성
    @Transactional
    public CommentResponse create(Long postId, CommentCreateRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new UserNotFoundException(request.userId()));

        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        Comment comment = new Comment(request.content(), user, post);
        commentRepository.save(comment);

        return CommentResponse.from(comment);
    }


    // 댓글 조회
    public List<CommentResponse> findByPostId(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new PostNotFoundException(postId));

        return commentRepository.findAllWithUser()
                .stream()
                .map(CommentResponse::from)
                .toList();
    }


    // 댓글 수정
    @Transactional
    public CommentResponse update(Long commentId, CommentUpdateRequest request) {

        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new CommentNotFoundException(commentId));

        comment.update(request.content());

        return CommentResponse.from(comment);
    }


    // 댓글 삭제
    @Transactional
    public void delete(Long commentId) {
        commentRepository.deleteById(commentId);
    }
}

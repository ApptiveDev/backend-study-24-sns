package com.example.sns.service;

import com.example.sns.dto.CommentCreateRequest;
import com.example.sns.dto.CommentResponse;
import com.example.sns.dto.CommentUpdateRequest;
import com.example.sns.entity.Comment;
import com.example.sns.entity.Post;
import com.example.sns.entity.User;
import com.example.sns.exception.PostNotFoundException;
import com.example.sns.exception.UserNotFoundException;
import com.example.sns.repository.PostRepository;
import com.example.sns.repository.UserRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Transactional
public class CommentCommandService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final EntityManager entityManager;

    public CommentResponse addComment(Long postId, CommentCreateRequest request) {
        Post post = findPostWithDetails(postId);
        User commenter = findUser(request.commenterId());

        Comment comment = post.addComment(request.content(), commenter);

        entityManager.flush();

        return CommentResponse.from(comment);
    }

    public CommentResponse editComment(Long postId, Long commentId, CommentUpdateRequest request) {
        Post post = findPostWithDetails(postId);
        User requester = findUser(request.requesterId());

        post.editComment(commentId, request.content(), requester);

        Comment editedComment = post.getComments().stream()
                .filter(comment -> comment.getId().equals(commentId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));

        return CommentResponse.from(editedComment);
    }

    public void removeComment(Long postId, Long commentId, Long requesterId) {
        Post post = findPostWithDetails(postId);
        User requester = findUser(requesterId);

        post.removeComment(commentId, requester);
    }

    private Post findPostWithDetails(Long postId) {
        return postRepository.findWithDetailsById(postId)
                .orElseThrow(PostNotFoundException::new);
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(UserNotFoundException::new);
    }
}



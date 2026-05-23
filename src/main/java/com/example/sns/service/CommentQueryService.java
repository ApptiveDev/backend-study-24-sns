package com.example.sns.service;

import com.example.sns.dto.CommentResponse;
import com.example.sns.entity.Post;
import com.example.sns.exception.PostNotFoundException;
import com.example.sns.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentQueryService {

    private final PostRepository postRepository;

    public List<CommentResponse> getComments(Long postId) {
        Post post = postRepository.findWithDetailsById(postId)
                .orElseThrow(PostNotFoundException::new);

        return post.getComments().stream()
                .map(CommentResponse::from)
                .toList();
    }
}
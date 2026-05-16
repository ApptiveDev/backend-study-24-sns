package com.example.sns.post.service;

import com.example.sns.comment.entity.Comment;
import com.example.sns.comment.repository.CommentRepository;
import com.example.sns.exception.PostNotFoundException;
import com.example.sns.exception.UserNotFoundException;
import com.example.sns.post.dto.PostCreateRequest;
import com.example.sns.post.dto.PostDetailResponse;
import com.example.sns.post.dto.PostResponse;
import com.example.sns.post.repository.PostRepository;
import com.example.sns.post.dto.PostUpdateRequest;
import com.example.sns.post.entity.Post;
import com.example.sns.postlike.PostLikeRepository;
import com.example.sns.user.entity.User;
import com.example.sns.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PostLikeRepository postLikeRepository;

    @Transactional
    public PostResponse create(PostCreateRequest request) {
        User user = userRepository.findById(request.userId())
                .orElseThrow(() -> new UserNotFoundException(request.userId()));

        Post post = new Post(request.title(), request.content(), user);
        postRepository.save(post);

        return PostResponse.from(post);
    }

    // 전체 조회
    public Page<PostResponse> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return postRepository.findAllWithUser(pageable)     // Fetch Join
                .map(PostResponse::from);
    }

    // Id별 조회
    public PostDetailResponse findByIdWithComments(Long postId) {
            Post post = postRepository.findById(postId)
                    .orElseThrow(() -> new PostNotFoundException(postId));

            List<Comment> comments = commentRepository.findByPostIdWithUser(post.getId());
            long likeCount = postLikeRepository.countByPostId(postId);

            return PostDetailResponse.from(post, comments, likeCount);
    }

    // 수정
    @Transactional
    public PostResponse update(Long postId, PostUpdateRequest request) {
        Post post = postRepository.findById(postId).orElseThrow(
                () -> new PostNotFoundException(postId));

        post.update(request.title(), request.content());

        //  postRepository.save(post);

        return PostResponse.from(post);
    }

    // 삭제
    @Transactional
    public void delete(Long postId) {
        postRepository.deleteById(postId);
    }

}

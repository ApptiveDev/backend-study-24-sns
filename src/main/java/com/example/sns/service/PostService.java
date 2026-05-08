package com.example.sns.service;

import com.example.sns.entity.Post;
import com.example.sns.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    public void createPost(Post post) {
        postRepository.save(post);
    }

    public List<Post> getPosts() {
        return postRepository.findAll();
    }

    @Transactional
    public void updatePost(Long id, Post postRequest) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        post.setTitle(postRequest.getTitle());
        post.setContent(postRequest.getContent());
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }
}
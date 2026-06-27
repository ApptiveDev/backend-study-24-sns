package com.example.sns.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.sns.entity.Post;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long>{
    List<Post> findByTitle(String title);
    List<Post> findByContent(String content);
}

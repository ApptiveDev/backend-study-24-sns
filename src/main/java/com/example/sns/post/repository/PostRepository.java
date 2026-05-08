package com.example.sns.post.repository;

import com.example.sns.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
//    private final JdbcClient jdbcClient;
//
//    public PostRepository(JdbcClient jdbcClient) {
//        this.jdbcClient = jdbcClient;
//    }
//
//    // 게시글 작성
//    public void save(Post post) {
//        jdbcClient.sql("""
//            INSERT INTO posts (title, content, created_at)
//            VALUES (?, ?, ?)
//            """)
//                .param("userId", post.getUserId())
//                .param("title", post.getTitle())
//                .param("content", post.getContent())
//                .param("createdAt", LocalDateTime.now())
//                .param("updatedAt", LocalDateTime.now())
//                .update();
//    }
//
//    // 전체 조회
//    public List<Post> findAll() {
//        return jdbcClient.sql("SELECT * FROM posts ORDER BY created_at DESC")
//                .query((rs, rowNum) -> new Post(
//                        rs.getLong("id"),
//                        rs.getLong("user_id"),
//                        rs.getString("title"),
//                        rs.getString("content"),
//                        rs.getTimestamp("created_at").toLocalDateTime(),
//                        rs.getTimestamp("updated_at").toLocalDateTime()
//                ))
//                .list();
//    }
//
//    // 게시물 단건 조회 (ID로)
//    public Optional<Post> findById(Long id) {
//        return jdbcClient.sql("SELECT * FROM posts WHERE id = :id")
//                .param("id", id)
//                .query((rs, rowNum) -> new Post(
//                        rs.getLong("id"),
//                        rs.getLong("user_id"),
//                        rs.getString("title"),
//                        rs.getString("content"),
//                        rs.getTimestamp("created_at").toLocalDateTime(),
//                        rs.getTimestamp("updated_at").toLocalDateTime()
//        ))
//                .optional();
//    }


}

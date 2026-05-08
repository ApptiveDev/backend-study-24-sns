package com.example.sns.user.repository;
import com.example.sns.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);


//    private final JdbcClient jdbcClient;
//
//    public UserRepository(JdbcClient jdbcClient) {
//        this.jdbcClient = jdbcClient;
//    }
//
//    // 사용자 생성
//    public void save(User user) {
//        jdbcClient.sql("""
//           INSERT INTO users(name, email, password, created_at)
//           VALUES (:name, :email, :password, :createdAt)
//           """)
//                .param("name", user.getName())
//                .param("email", user.getEmail())
//                .param("password", user.getPassword())
//                .param("createdAt", LocalDateTime.now())
//                .update();
//    }

}

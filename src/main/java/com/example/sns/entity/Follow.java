package com.example.sns.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(
        name = "follows",
        // 같은 사람을 중복 팔로우하지 못하도록 제약
        uniqueConstraints = @UniqueConstraint(columnNames = {"follower_id", "following_id"})
)
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "follow_id")
    private Long id;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // 팔로우를 "하는" 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follower_id")
    private User follower;

    // 팔로우를 "받는" 사람
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id")
    private User following;

    // 외부에서 직접 new로 생성하지 못하도록 private 생성자 선언
    private Follow(User follower, User following) {
        this.follower = follower;
        this.following = following;
    }

    // 팔로우 생성을 담당하는 public static 팩토리 메서드
    public static Follow create(User follower, User following) {
        if (follower == null) {
            throw new IllegalArgumentException("팔로우하는 사용자는 필수입니다.");
        }
        if (following == null) {
            throw new IllegalArgumentException("팔로우받는 사용자는 필수입니다.");
        }
        return new Follow(follower, following);
    }
}

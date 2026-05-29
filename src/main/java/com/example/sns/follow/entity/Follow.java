package com.example.sns.follow.entity;

import com.example.sns.user.entity.User;
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
@Table(uniqueConstraints = {    // UNIQUE 제약: 같은 사람을 중복 팔로우 못하게
        @UniqueConstraint(
                columnNames = {"follower_id", "following_id"}
        )
})
@EntityListeners(AuditingEntityListener.class)
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "follwer_id")
    private User follower;  // 팔로우 하는 사람

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "following_id")
    private User following;  // 팔로우 받는 사람

    @CreatedDate
    private LocalDateTime createdAt;


    private Follow(User follower, User following) {
        this.follower = follower;
        this.following = following;
    }


    public static Follow create(User follower, User following) {
        validateNotSelf(follower, following);
        return new Follow(follower, following);
    }


    private static void validateNotSelf(User follower, User following) {
        if (follower.getId().equals(following.getId())) {
            throw new IllegalArgumentException("자신을 팔로우할 수 없습니다.");
        }
    }
}

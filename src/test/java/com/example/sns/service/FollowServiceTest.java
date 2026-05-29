package com.example.sns.service;

import com.example.sns.entity.User;
import com.example.sns.exception.BadRequestException;
import com.example.sns.exception.ErrorCode;
import com.example.sns.repository.FollowRepository;
import com.example.sns.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FollowServiceTest {

    @Mock
    private FollowRepository followRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private FollowService followService;

    @Test
    void follow_whenTargetIsMe_throwsCannotFollowSelf() {
        assertThatThrownBy(() -> followService.follow(1L, 1L))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.CANNOT_FOLLOW_SELF);

        verify(followRepository, never()).save(org.mockito.ArgumentMatchers.any());
    }

    @Test
    void follow_whenAlreadyExists_throwsFollowAlreadyExists() {
        User follower = User.create("dex", "Dex");
        User following = User.create("neo", "Neo");
        ReflectionTestUtils.setField(follower, "id", 1L);
        ReflectionTestUtils.setField(following, "id", 2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(follower));
        when(userRepository.findById(2L)).thenReturn(Optional.of(following));
        when(followRepository.existsByFollowerAndFollowing(follower, following)).thenReturn(true);

        assertThatThrownBy(() -> followService.follow(2L, 1L))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.FOLLOW_ALREADY_EXISTS);
    }
}

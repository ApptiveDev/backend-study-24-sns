package com.example.sns.service;

import com.example.sns.dto.UserCreateRequest;
import com.example.sns.exception.BadRequestException;
import com.example.sns.exception.ErrorCode;
import com.example.sns.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @Test
    void create_whenUsernameAlreadyExists_throwsDuplicateUsername() {
        UserCreateRequest request = new UserCreateRequest();
        ReflectionTestUtils.setField(request, "username", "dex");
        ReflectionTestUtils.setField(request, "nickname", "Dex");
        when(userRepository.existsByUsername("dex")).thenReturn(true);

        assertThatThrownBy(() -> userService.create(request))
                .isInstanceOf(BadRequestException.class)
                .extracting("errorCode")
                .isEqualTo(ErrorCode.DUPLICATE_USERNAME);
    }
}

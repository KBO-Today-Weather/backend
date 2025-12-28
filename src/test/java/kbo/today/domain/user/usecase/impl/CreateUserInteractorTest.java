package kbo.today.domain.user.usecase.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import kbo.today.common.exception.DuplicateEmailException;
import kbo.today.domain.user.domain.User;
import kbo.today.domain.user.port.PasswordEncoderPort;
import kbo.today.domain.user.port.UserRepositoryPort;
import kbo.today.domain.user.usecase.CreateUserCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateUserInteractor 단위 테스트")
class CreateUserInteractorTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private PasswordEncoderPort passwordEncoder;

    @InjectMocks
    private CreateUserInteractor createUserInteractor;

    private CreateUserCommand command;
    private User savedUser;

    @BeforeEach
    void setUp() {
        command = new CreateUserCommand("test@example.com", "password123", "테스트유저");
        savedUser = new User(1L, "test@example.com", "encodedPassword", "테스트유저");
    }

    @Test
    @DisplayName("새로운 사용자를 성공적으로 생성한다")
    void createUser_Success() {
        // given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // when
        User result = createUserInteractor.create(command);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getEmail()).isEqualTo("test@example.com");
        assertThat(result.getNickname()).isEqualTo("테스트유저");
        assertThat(result.getPassword()).isEqualTo("encodedPassword");

        verify(userRepository).findByEmail("test@example.com");
        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    @DisplayName("이미 존재하는 이메일로 회원가입 시 예외를 발생시킨다")
    void createUser_DuplicateEmail_ThrowsException() {
        // given
        User existingUser = new User(1L, "test@example.com", "encodedPassword", "기존유저");
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(existingUser));

        // when & then
        assertThatThrownBy(() -> createUserInteractor.create(command))
            .isInstanceOf(DuplicateEmailException.class)
            .hasMessageContaining("Email already exists: test@example.com");

        verify(userRepository).findByEmail("test@example.com");
        verify(passwordEncoder, never()).encode(anyString());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    @DisplayName("비밀번호가 암호화되어 저장된다")
    void createUser_PasswordIsEncoded() {
        // given
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword123");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            return new User(1L, user.getEmail(), user.getPassword(), user.getNickname());
        });

        // when
        User result = createUserInteractor.create(command);

        // then
        assertThat(result.getPassword()).isEqualTo("encodedPassword123");
        assertThat(result.getPassword()).isNotEqualTo("password123");
        verify(passwordEncoder).encode("password123");
    }
}


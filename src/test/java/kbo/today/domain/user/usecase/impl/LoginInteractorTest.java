package kbo.today.domain.user.usecase.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import kbo.today.common.exception.InvalidCredentialsException;
import kbo.today.domain.user.domain.User;
import kbo.today.domain.user.enumerable.UserRole;
import kbo.today.domain.user.port.JwtTokenPort;
import kbo.today.domain.user.port.PasswordEncoderPort;
import kbo.today.domain.user.port.UserRepositoryPort;
import kbo.today.domain.user.usecase.LoginCommand;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("LoginInteractor 단위 테스트")
class LoginInteractorTest {

    @Mock
    private UserRepositoryPort userRepository;

    @Mock
    private PasswordEncoderPort passwordEncoder;

    @Mock
    private JwtTokenPort jwtTokenPort;

    @InjectMocks
    private LoginInteractor loginInteractor;

    private LoginCommand command;
    private User user;

    @BeforeEach
    void setUp() {
        command = new LoginCommand("test@example.com", "password123");
        user = new User(1L, "test@example.com", "encodedPassword", "테스트유저");
    }

    @Test
    @DisplayName("올바른 이메일과 비밀번호로 로그인에 성공한다")
    void login_Success() {
        // given
        String expectedToken = "jwt.token.here";
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "encodedPassword")).thenReturn(true);
        when(jwtTokenPort.generateToken(1L, "test@example.com", "USER")).thenReturn(expectedToken);

        // when
        String token = loginInteractor.login(command);

        // then
        assertThat(token).isEqualTo(expectedToken);
        verify(userRepository).findByEmail("test@example.com");
        verify(passwordEncoder).matches("password123", "encodedPassword");
        verify(jwtTokenPort).generateToken(1L, "test@example.com", "USER");
    }

    @Test
    @DisplayName("존재하지 않는 이메일로 로그인 시 예외를 발생시킨다")
    void login_UserNotFound_ThrowsException() {
        // given
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> loginInteractor.login(command))
            .isInstanceOf(InvalidCredentialsException.class);

        verify(userRepository).findByEmail("test@example.com");
        verify(passwordEncoder, never()).matches(anyString(), anyString());
        verify(jwtTokenPort, never()).generateToken(anyLong(), anyString(), anyString());
    }

    @Test
    @DisplayName("잘못된 비밀번호로 로그인 시 예외를 발생시킨다")
    void login_InvalidPassword_ThrowsException() {
        // given
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        // when & then
        LoginCommand wrongPasswordCommand = new LoginCommand("test@example.com", "wrongPassword");
        assertThatThrownBy(() -> loginInteractor.login(wrongPasswordCommand))
            .isInstanceOf(InvalidCredentialsException.class);

        verify(userRepository).findByEmail("test@example.com");
        verify(passwordEncoder).matches("wrongPassword", "encodedPassword");
        verify(jwtTokenPort, never()).generateToken(anyLong(), anyString(), anyString());
    }
}


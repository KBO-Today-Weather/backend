package kbo.today.domain.user.usecase.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kbo.today.common.exception.DuplicateEmailException;
import kbo.today.domain.user.domain.User;
import kbo.today.domain.user.port.PasswordEncoderPort;
import kbo.today.domain.user.port.UserRepositoryPort;
import kbo.today.domain.user.usecase.CreateUserCommand;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
@DisplayName("CreateUserInteractor 통합 테스트")
class CreateUserInteractorIntegrationTest {

    @Autowired
    private UserRepositoryPort userRepositoryPort;

    @Autowired
    private PasswordEncoderPort passwordEncoderPort;

    @Test
    @DisplayName("실제 Repository를 사용하여 사용자를 생성한다")
    void createUser_WithRealRepository_Success() {
        // given
        CreateUserInteractor interactor = new CreateUserInteractor(userRepositoryPort, passwordEncoderPort);
        CreateUserCommand command = new CreateUserCommand("test@example.com", "password123", "테스트유저");

        // when
        User created = interactor.create(command);

        // then
        assertThat(created.getId()).isNotNull();
        assertThat(created.getEmail()).isEqualTo("test@example.com");
        assertThat(created.getNickname()).isEqualTo("테스트유저");
        assertThat(created.getPassword()).isNotEqualTo("password123"); // 암호화됨

        // 실제 DB에서 조회 확인
        var found = userRepositoryPort.findByEmail("test@example.com");
        assertThat(found).isPresent();
        assertThat(found.get().getId()).isEqualTo(created.getId());
    }

    @Test
    @DisplayName("이미 존재하는 이메일로 회원가입 시 예외를 발생시킨다")
    void createUser_DuplicateEmail_ThrowsException() {
        // given
        CreateUserInteractor interactor = new CreateUserInteractor(userRepositoryPort, passwordEncoderPort);
        CreateUserCommand command1 = new CreateUserCommand("test@example.com", "password123", "테스트유저1");
        CreateUserCommand command2 = new CreateUserCommand("test@example.com", "password456", "테스트유저2");

        interactor.create(command1);

        // when & then
        assertThatThrownBy(() -> interactor.create(command2))
            .isInstanceOf(DuplicateEmailException.class)
            .hasMessageContaining("Email already exists: test@example.com");
    }

    @Test
    @DisplayName("비밀번호가 실제로 암호화되어 저장된다")
    void createUser_PasswordIsActuallyEncoded() {
        // given
        CreateUserInteractor interactor = new CreateUserInteractor(userRepositoryPort, passwordEncoderPort);
        CreateUserCommand command = new CreateUserCommand("test@example.com", "password123", "테스트유저");

        // when
        User created = interactor.create(command);

        // then
        assertThat(created.getPassword()).isNotEqualTo("password123");
        assertThat(created.getPassword()).startsWith("$2a$"); // BCrypt 해시 형식

        // 암호화된 비밀번호로 검증 가능한지 확인
        boolean matches = passwordEncoderPort.matches("password123", created.getPassword());
        assertThat(matches).isTrue();
    }
}


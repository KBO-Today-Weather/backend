package kbo.today.adapter.out.security;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@DisplayName("PasswordEncoderAdapter 테스트")
class PasswordEncoderAdapterTest {

    private PasswordEncoderAdapter passwordEncoderAdapter;
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = new BCryptPasswordEncoder();
        passwordEncoderAdapter = new PasswordEncoderAdapter(passwordEncoder);
    }

    @Test
    @DisplayName("비밀번호를 성공적으로 인코딩한다")
    void encode_Success() {
        // given
        String rawPassword = "password123";

        // when
        String encodedPassword = passwordEncoderAdapter.encode(rawPassword);

        // then
        assertThat(encodedPassword).isNotNull();
        assertThat(encodedPassword).isNotEqualTo(rawPassword);
        assertThat(encodedPassword.length()).isGreaterThan(50); // BCrypt 해시 길이
    }

    @Test
    @DisplayName("같은 비밀번호를 인코딩하면 다른 해시가 생성된다")
    void encode_SamePassword_DifferentHash() {
        // given
        String rawPassword = "password123";

        // when
        String encodedPassword1 = passwordEncoderAdapter.encode(rawPassword);
        String encodedPassword2 = passwordEncoderAdapter.encode(rawPassword);

        // then
        assertThat(encodedPassword1).isNotEqualTo(encodedPassword2);
    }

    @Test
    @DisplayName("올바른 비밀번호를 검증한다")
    void matches_CorrectPassword_ReturnsTrue() {
        // given
        String rawPassword = "password123";
        String encodedPassword = passwordEncoderAdapter.encode(rawPassword);

        // when
        boolean matches = passwordEncoderAdapter.matches(rawPassword, encodedPassword);

        // then
        assertThat(matches).isTrue();
    }

    @Test
    @DisplayName("잘못된 비밀번호를 검증 시 false를 반환한다")
    void matches_IncorrectPassword_ReturnsFalse() {
        // given
        String rawPassword = "password123";
        String wrongPassword = "wrongPassword";
        String encodedPassword = passwordEncoderAdapter.encode(rawPassword);

        // when
        boolean matches = passwordEncoderAdapter.matches(wrongPassword, encodedPassword);

        // then
        assertThat(matches).isFalse();
    }

    @Test
    @DisplayName("빈 비밀번호를 인코딩한다")
    void encode_EmptyPassword_Success() {
        // given
        String emptyPassword = "";

        // when
        String encodedPassword = passwordEncoderAdapter.encode(emptyPassword);

        // then
        assertThat(encodedPassword).isNotNull();
    }
}


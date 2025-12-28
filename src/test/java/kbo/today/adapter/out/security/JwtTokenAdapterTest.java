package kbo.today.adapter.out.security;

import static org.assertj.core.api.Assertions.assertThat;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("JwtTokenAdapter 테스트")
class JwtTokenAdapterTest {

    private JwtTokenAdapter jwtTokenAdapter;

    @BeforeEach
    void setUp() {
        String secret = "testSecretKeyForTestingPurposesOnlyMustBeLongEnough";
        long expirationTime = 3600000; // 1 hour
        jwtTokenAdapter = new JwtTokenAdapter(secret, expirationTime);
    }

    @Test
    @DisplayName("JWT 토큰을 성공적으로 생성한다")
    void generateToken_Success() {
        // given
        Long userId = 1L;
        String email = "test@example.com";
        String role = "USER";

        // when
        String token = jwtTokenAdapter.generateToken(userId, email, role);

        // then
        assertThat(token).isNotNull();
        assertThat(token.split("\\.")).hasSize(3); // JWT는 3부분으로 구성
    }

    @Test
    @DisplayName("생성된 토큰을 파싱하여 정보를 추출한다")
    void parseToken_Success() {
        // given
        Long userId = 1L;
        String email = "test@example.com";
        String role = "USER";
        String token = jwtTokenAdapter.generateToken(userId, email, role);

        // when
        Claims claims = jwtTokenAdapter.parseToken(token);

        // then
        assertThat(claims.getSubject()).isEqualTo(String.valueOf(userId));
        assertThat(claims.get("email", String.class)).isEqualTo(email);
        assertThat(claims.get("role", String.class)).isEqualTo(role);
        assertThat(claims.getIssuedAt()).isNotNull();
        assertThat(claims.getExpiration()).isNotNull();
    }

    @Test
    @DisplayName("유효한 토큰을 검증한다")
    void validateToken_ValidToken_ReturnsTrue() {
        // given
        String token = jwtTokenAdapter.generateToken(1L, "test@example.com", "USER");

        // when
        boolean isValid = jwtTokenAdapter.validateToken(token);

        // then
        assertThat(isValid).isTrue();
    }

    @Test
    @DisplayName("잘못된 토큰을 검증 시 false를 반환한다")
    void validateToken_InvalidToken_ReturnsFalse() {
        // given
        String invalidToken = "invalid.token.here";

        // when
        boolean isValid = jwtTokenAdapter.validateToken(invalidToken);

        // then
        assertThat(isValid).isFalse();
    }

    @Test
    @DisplayName("빈 토큰을 검증 시 false를 반환한다")
    void validateToken_EmptyToken_ReturnsFalse() {
        // given
        String emptyToken = "";

        // when
        boolean isValid = jwtTokenAdapter.validateToken(emptyToken);

        // then
        assertThat(isValid).isFalse();
    }
}


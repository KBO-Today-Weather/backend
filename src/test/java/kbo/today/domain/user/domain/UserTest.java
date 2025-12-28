package kbo.today.domain.user.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kbo.today.domain.user.enumerable.UserRole;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("User 도메인 엔티티 단위 테스트")
class UserTest {

    @Test
    @DisplayName("create() 팩토리 메서드로 User를 생성한다")
    void create_Success() {
        // when
        User user = User.create("test@example.com", "password123", "테스트유저");

        // then
        assertThat(user).isNotNull();
        assertThat(user.getId()).isNull();
        assertThat(user.getEmail()).isEqualTo("test@example.com");
        assertThat(user.getPassword()).isEqualTo("password123");
        assertThat(user.getNickname()).isEqualTo("테스트유저");
        assertThat(user.getRole()).isEqualTo(UserRole.USER);
    }

    @Test
    @DisplayName("withId() 메서드로 ID를 설정한 새로운 User를 생성한다")
    void withId_Success() {
        // given
        User user = User.create("test@example.com", "password123", "테스트유저");
        Long id = 1L;

        // when
        User userWithId = user.withId(id);

        // then
        assertThat(userWithId.getId()).isEqualTo(id);
        assertThat(userWithId.getEmail()).isEqualTo(user.getEmail());
        assertThat(userWithId.getPassword()).isEqualTo(user.getPassword());
        assertThat(userWithId.getNickname()).isEqualTo(user.getNickname());
        assertThat(user.getId()).isNull(); // 원본은 변경되지 않음
    }

    @Test
    @DisplayName("생성자에서 email이 null이면 예외가 발생한다")
    void constructor_NullEmail_ThrowsException() {
        // when & then
        assertThatThrownBy(() -> new User(null, null, "password123", "테스트유저"))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("생성자에서 password가 null이면 예외가 발생한다")
    void constructor_NullPassword_ThrowsException() {
        // when & then
        assertThatThrownBy(() -> new User(null, "test@example.com", null, "테스트유저"))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("생성자에서 nickname이 null이면 예외가 발생한다")
    void constructor_NullNickname_ThrowsException() {
        // when & then
        assertThatThrownBy(() -> new User(null, "test@example.com", "password123", null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("생성자에서 role은 항상 USER로 설정된다")
    void constructor_RoleIsAlwaysUser() {
        // when
        User user = new User(1L, "test@example.com", "password123", "테스트유저");

        // then
        assertThat(user.getRole()).isEqualTo(UserRole.USER);
    }
}


package kbo.today.domain.team;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Team 도메인 엔티티 단위 테스트")
class TeamTest {

    @Test
    @DisplayName("create() 팩토리 메서드로 Team을 생성한다")
    void create_Success() {
        // when
        Team team = Team.create("한화 이글스", "대전", "logo.png");

        // then
        assertThat(team).isNotNull();
        assertThat(team.getId()).isNull();
        assertThat(team.getName()).isEqualTo("한화 이글스");
        assertThat(team.getCity()).isEqualTo("대전");
        assertThat(team.getLogoUrl()).isEqualTo("logo.png");
        assertThat(team.getStatus()).isEqualTo(TeamStatus.ACTIVE);
    }

    @Test
    @DisplayName("fromPersistence() 팩토리 메서드로 Team을 생성한다")
    void fromPersistence_Success() {
        // given
        Long id = 1L;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // when
        Team team = Team.fromPersistence(id, "한화 이글스", "대전", "logo.png", TeamStatus.ACTIVE,
            createdAt, updatedAt, null);

        // then
        assertThat(team.getId()).isEqualTo(id);
        assertThat(team.getName()).isEqualTo("한화 이글스");
        assertThat(team.getCity()).isEqualTo("대전");
        assertThat(team.getLogoUrl()).isEqualTo("logo.png");
        assertThat(team.getStatus()).isEqualTo(TeamStatus.ACTIVE);
        assertThat(team.getCreatedAt()).isEqualTo(createdAt);
        assertThat(team.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    @DisplayName("withId() 메서드로 ID를 설정한 새로운 Team을 생성한다")
    void withId_Success() {
        // given
        Team team = Team.create("한화 이글스", "대전", "logo.png");
        Long id = 1L;

        // when
        Team teamWithId = team.withId(id);

        // then
        assertThat(teamWithId.getId()).isEqualTo(id);
        assertThat(teamWithId.getName()).isEqualTo(team.getName());
        assertThat(teamWithId.getCity()).isEqualTo(team.getCity());
        assertThat(team.getId()).isNull(); // 원본은 변경되지 않음
    }

    @Test
    @DisplayName("fromPersistence()에서 status가 null이면 ACTIVE로 설정된다")
    void fromPersistence_NullStatus_DefaultsToActive() {
        // given
        Long id = 1L;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // when
        Team team = Team.fromPersistence(id, "한화 이글스", "대전", "logo.png", null,
            createdAt, updatedAt, null);

        // then
        assertThat(team.getStatus()).isEqualTo(TeamStatus.ACTIVE);
    }

    @Test
    @DisplayName("fromPersistence()에서 name이 null이면 예외가 발생한다")
    void fromPersistence_NullName_ThrowsException() {
        // given
        Long id = 1L;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // when & then
        assertThatThrownBy(() -> Team.fromPersistence(id, null, "대전", "logo.png", TeamStatus.ACTIVE,
            createdAt, updatedAt, null))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("fromPersistence()에서 city가 null이면 예외가 발생한다")
    void fromPersistence_NullCity_ThrowsException() {
        // given
        Long id = 1L;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // when & then
        assertThatThrownBy(() -> Team.fromPersistence(id, "한화 이글스", null, "logo.png", TeamStatus.ACTIVE,
            createdAt, updatedAt, null))
            .isInstanceOf(NullPointerException.class);
    }
}


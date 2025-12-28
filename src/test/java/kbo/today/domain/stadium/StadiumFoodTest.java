package kbo.today.domain.stadium;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import kbo.today.domain.team.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("StadiumFood 도메인 엔티티 단위 테스트")
class StadiumFoodTest {

    private Team team;
    private Stadium stadium;

    @BeforeEach
    void setUp() {
        team = Team.create("한화 이글스", "대전", "logo.png");
        stadium = Stadium.create(team, "대전 한화생명 이글스파크", "대전광역시 중구", 20000);
    }

    @Test
    @DisplayName("create() 팩토리 메서드로 StadiumFood를 생성한다")
    void create_Success() {
        // when
        StadiumFood food = StadiumFood.create(stadium, "치킨", 15000, "1루석", 4.5);

        // then
        assertThat(food).isNotNull();
        assertThat(food.getId()).isNull();
        assertThat(food.getStadium()).isEqualTo(stadium);
        assertThat(food.getName()).isEqualTo("치킨");
        assertThat(food.getPrice()).isEqualTo(15000);
        assertThat(food.getLocation()).isEqualTo("1루석");
        assertThat(food.getRating()).isEqualTo(4.5);
    }

    @Test
    @DisplayName("fromPersistence() 팩토리 메서드로 StadiumFood를 생성한다")
    void fromPersistence_Success() {
        // given
        Long id = 1L;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // when
        StadiumFood food = StadiumFood.fromPersistence(
            id, stadium, "치킨", 15000, "1루석", 4.5,
            createdAt, updatedAt, null
        );

        // then
        assertThat(food.getId()).isEqualTo(id);
        assertThat(food.getName()).isEqualTo("치킨");
        assertThat(food.getPrice()).isEqualTo(15000);
        assertThat(food.getCreatedAt()).isEqualTo(createdAt);
        assertThat(food.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    @DisplayName("withId() 메서드로 ID를 설정한 새로운 StadiumFood를 생성한다")
    void withId_Success() {
        // given
        StadiumFood food = StadiumFood.create(stadium, "치킨", 15000, "1루석", 4.5);
        Long id = 1L;

        // when
        StadiumFood foodWithId = food.withId(id);

        // then
        assertThat(foodWithId.getId()).isEqualTo(id);
        assertThat(foodWithId.getName()).isEqualTo(food.getName());
        assertThat(food.getId()).isNull(); // 원본은 변경되지 않음
    }

    @Test
    @DisplayName("fromPersistence()에서 name이 null이면 예외가 발생한다")
    void fromPersistence_NullName_ThrowsException() {
        // given
        Long id = 1L;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // when & then
        assertThatThrownBy(() -> StadiumFood.fromPersistence(
            id, stadium, null, 15000, "1루석", 4.5,
            createdAt, updatedAt, null
        )).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("fromPersistence()에서 price가 null이면 예외가 발생한다")
    void fromPersistence_NullPrice_ThrowsException() {
        // given
        Long id = 1L;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // when & then
        assertThatThrownBy(() -> StadiumFood.fromPersistence(
            id, stadium, "치킨", null, "1루석", 4.5,
            createdAt, updatedAt, null
        )).isInstanceOf(NullPointerException.class);
    }
}


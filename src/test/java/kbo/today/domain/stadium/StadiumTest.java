package kbo.today.domain.stadium;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kbo.today.domain.team.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Stadium 도메인 엔티티 단위 테스트")
class StadiumTest {

    private Team team;

    @BeforeEach
    void setUp() {
        team = Team.create("한화 이글스", "대전", "logo.png");
    }

    @Test
    @DisplayName("create() 팩토리 메서드로 Stadium을 생성한다 (위치 정보 없음)")
    void create_WithoutLocation_Success() {
        // when
        Stadium stadium = Stadium.create(team, "대전 한화생명 이글스파크", "대전광역시 중구", 20000);

        // then
        assertThat(stadium).isNotNull();
        assertThat(stadium.getId()).isNull();
        assertThat(stadium.getTeam()).isEqualTo(team);
        assertThat(stadium.getName()).isEqualTo("대전 한화생명 이글스파크");
        assertThat(stadium.getAddress()).isEqualTo("대전광역시 중구");
        assertThat(stadium.getCapacity()).isEqualTo(20000);
        assertThat(stadium.getLatitude()).isNull();
        assertThat(stadium.getLongitude()).isNull();
        assertThat(stadium.getFoods()).isEmpty();
        assertThat(stadium.getSeats()).isEmpty();
        assertThat(stadium.getTransports()).isEmpty();
    }

    @Test
    @DisplayName("create() 팩토리 메서드로 Stadium을 생성한다 (위치 정보 포함)")
    void create_WithLocation_Success() {
        // when
        Stadium stadium = Stadium.create(team, "대전 한화생명 이글스파크", "대전광역시 중구", 20000, 36.3174, 127.4288);

        // then
        assertThat(stadium).isNotNull();
        assertThat(stadium.getLatitude()).isEqualTo(36.3174);
        assertThat(stadium.getLongitude()).isEqualTo(127.4288);
    }

    @Test
    @DisplayName("fromPersistence() 팩토리 메서드로 Stadium을 생성한다")
    void fromPersistence_Success() {
        // given
        Long id = 1L;
        List<StadiumFood> foods = new ArrayList<>();
        List<StadiumSeat> seats = new ArrayList<>();
        List<StadiumTransport> transports = new ArrayList<>();
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // when
        Stadium stadium = Stadium.fromPersistence(
            id, team, "대전 한화생명 이글스파크", "대전광역시 중구", 20000,
            36.3174, 127.4288,
            foods, seats, transports,
            createdAt, updatedAt, null
        );

        // then
        assertThat(stadium.getId()).isEqualTo(id);
        assertThat(stadium.getTeam()).isEqualTo(team);
        assertThat(stadium.getName()).isEqualTo("대전 한화생명 이글스파크");
        assertThat(stadium.getAddress()).isEqualTo("대전광역시 중구");
        assertThat(stadium.getCapacity()).isEqualTo(20000);
        assertThat(stadium.getLatitude()).isEqualTo(36.3174);
        assertThat(stadium.getLongitude()).isEqualTo(127.4288);
        assertThat(stadium.getCreatedAt()).isEqualTo(createdAt);
        assertThat(stadium.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    @DisplayName("withId() 메서드로 ID를 설정한 새로운 Stadium을 생성한다")
    void withId_Success() {
        // given
        Stadium stadium = Stadium.create(team, "대전 한화생명 이글스파크", "대전광역시 중구", 20000);
        Long id = 1L;

        // when
        Stadium stadiumWithId = stadium.withId(id);

        // then
        assertThat(stadiumWithId.getId()).isEqualTo(id);
        assertThat(stadiumWithId.getName()).isEqualTo(stadium.getName());
        assertThat(stadium.getId()).isNull(); // 원본은 변경되지 않음
    }

    @Test
    @DisplayName("updateLocation() 메서드로 위치 정보를 업데이트한다")
    void updateLocation_Success() {
        // given
        Stadium stadium = Stadium.create(team, "대전 한화생명 이글스파크", "대전광역시 중구", 20000);
        Double newLatitude = 36.3174;
        Double newLongitude = 127.4288;

        // when
        stadium.updateLocation(newLatitude, newLongitude);

        // then
        assertThat(stadium.getLatitude()).isEqualTo(newLatitude);
        assertThat(stadium.getLongitude()).isEqualTo(newLongitude);
    }

    @Test
    @DisplayName("fromPersistence()에서 name이 null이면 예외가 발생한다")
    void fromPersistence_NullName_ThrowsException() {
        // given
        Long id = 1L;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // when & then
        assertThatThrownBy(() -> Stadium.fromPersistence(
            id, team, null, "대전광역시 중구", 20000,
            36.3174, 127.4288,
            new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
            createdAt, updatedAt, null
        )).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("fromPersistence()에서 address가 null이면 예외가 발생한다")
    void fromPersistence_NullAddress_ThrowsException() {
        // given
        Long id = 1L;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // when & then
        assertThatThrownBy(() -> Stadium.fromPersistence(
            id, team, "대전 한화생명 이글스파크", null, 20000,
            36.3174, 127.4288,
            new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
            createdAt, updatedAt, null
        )).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("fromPersistence()에서 foods가 null이면 빈 리스트로 설정된다")
    void fromPersistence_NullFoods_DefaultsToEmptyList() {
        // given
        Long id = 1L;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // when
        Stadium stadium = Stadium.fromPersistence(
            id, team, "대전 한화생명 이글스파크", "대전광역시 중구", 20000,
            36.3174, 127.4288,
            null, null, null,
            createdAt, updatedAt, null
        );

        // then
        assertThat(stadium.getFoods()).isEmpty();
        assertThat(stadium.getSeats()).isEmpty();
        assertThat(stadium.getTransports()).isEmpty();
    }

    @Test
    @DisplayName("getFoods()는 null이면 빈 리스트를 반환한다")
    void getFoods_Null_ReturnsEmptyList() {
        // given
        Stadium stadium = Stadium.create(team, "대전 한화생명 이글스파크", "대전광역시 중구", 20000);

        // when & then
        assertThat(stadium.getFoods()).isEmpty();
    }
}


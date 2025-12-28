package kbo.today.domain.stadium;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import kbo.today.domain.team.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("StadiumSeat 도메인 엔티티 단위 테스트")
class StadiumSeatTest {

    private Team team;
    private Stadium stadium;

    @BeforeEach
    void setUp() {
        team = Team.create("한화 이글스", "대전", "logo.png");
        stadium = Stadium.create(team, "대전 한화생명 이글스파크", "대전광역시 중구", 20000);
    }

    @Test
    @DisplayName("create() 팩토리 메서드로 StadiumSeat를 생성한다")
    void create_Success() {
        // when
        StadiumSeat seat = StadiumSeat.create(stadium, "1루석", SeatType.PREMIUM_SEAT, "1루석 설명", "추천");

        // then
        assertThat(seat).isNotNull();
        assertThat(seat.getId()).isNull();
        assertThat(seat.getStadium()).isEqualTo(stadium);
        assertThat(seat.getSectionName()).isEqualTo("1루석");
        assertThat(seat.getSeatType()).isEqualTo(SeatType.PREMIUM_SEAT);
        assertThat(seat.getDescription()).isEqualTo("1루석 설명");
        assertThat(seat.getRecommendation()).isEqualTo("추천");
    }

    @Test
    @DisplayName("fromPersistence() 팩토리 메서드로 StadiumSeat를 생성한다")
    void fromPersistence_Success() {
        // given
        Long id = 1L;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // when
        StadiumSeat seat = StadiumSeat.fromPersistence(
            id, stadium, "1루석", SeatType.PREMIUM_SEAT, "1루석 설명", "추천",
            createdAt, updatedAt, null
        );

        // then
        assertThat(seat.getId()).isEqualTo(id);
        assertThat(seat.getSectionName()).isEqualTo("1루석");
        assertThat(seat.getSeatType()).isEqualTo(SeatType.PREMIUM_SEAT);
        assertThat(seat.getCreatedAt()).isEqualTo(createdAt);
        assertThat(seat.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    @DisplayName("withId() 메서드로 ID를 설정한 새로운 StadiumSeat를 생성한다")
    void withId_Success() {
        // given
        StadiumSeat seat = StadiumSeat.create(stadium, "1루석", SeatType.PREMIUM_SEAT, "1루석 설명", "추천");
        Long id = 1L;

        // when
        StadiumSeat seatWithId = seat.withId(id);

        // then
        assertThat(seatWithId.getId()).isEqualTo(id);
        assertThat(seatWithId.getSectionName()).isEqualTo(seat.getSectionName());
        assertThat(seat.getId()).isNull(); // 원본은 변경되지 않음
    }

    @Test
    @DisplayName("fromPersistence()에서 sectionName이 null이면 예외가 발생한다")
    void fromPersistence_NullSectionName_ThrowsException() {
        // given
        Long id = 1L;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // when & then
        assertThatThrownBy(() -> StadiumSeat.fromPersistence(
            id, stadium, null, SeatType.PREMIUM_SEAT, "1루석 설명", "추천",
            createdAt, updatedAt, null
        )).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("fromPersistence()에서 seatType이 null이면 예외가 발생한다")
    void fromPersistence_NullSeatType_ThrowsException() {
        // given
        Long id = 1L;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // when & then
        assertThatThrownBy(() -> StadiumSeat.fromPersistence(
            id, stadium, "1루석", null, "1루석 설명", "추천",
            createdAt, updatedAt, null
        )).isInstanceOf(NullPointerException.class);
    }
}


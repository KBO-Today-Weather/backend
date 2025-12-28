package kbo.today.domain.stadium;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import kbo.today.domain.team.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("StadiumTransport 도메인 엔티티 단위 테스트")
class StadiumTransportTest {

    private Team team;
    private Stadium stadium;

    @BeforeEach
    void setUp() {
        team = Team.create("한화 이글스", "대전", "logo.png");
        stadium = Stadium.create(team, "대전 한화생명 이글스파크", "대전광역시 중구", 20000);
    }

    @Test
    @DisplayName("create() 팩토리 메서드로 StadiumTransport를 생성한다")
    void create_Success() {
        // when
        StadiumTransport transport = StadiumTransport.create(
            stadium, TransportType.SUBWAY, "1호선 대전역", "지하철 설명", "팁"
        );

        // then
        assertThat(transport).isNotNull();
        assertThat(transport.getId()).isNull();
        assertThat(transport.getStadium()).isEqualTo(stadium);
        assertThat(transport.getTransportType()).isEqualTo(TransportType.SUBWAY);
        assertThat(transport.getRoute()).isEqualTo("1호선 대전역");
        assertThat(transport.getDescription()).isEqualTo("지하철 설명");
        assertThat(transport.getTip()).isEqualTo("팁");
    }

    @Test
    @DisplayName("fromPersistence() 팩토리 메서드로 StadiumTransport를 생성한다")
    void fromPersistence_Success() {
        // given
        Long id = 1L;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // when
        StadiumTransport transport = StadiumTransport.fromPersistence(
            id, stadium, TransportType.SUBWAY, "1호선 대전역", "지하철 설명", "팁",
            createdAt, updatedAt, null
        );

        // then
        assertThat(transport.getId()).isEqualTo(id);
        assertThat(transport.getTransportType()).isEqualTo(TransportType.SUBWAY);
        assertThat(transport.getRoute()).isEqualTo("1호선 대전역");
        assertThat(transport.getCreatedAt()).isEqualTo(createdAt);
        assertThat(transport.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    @DisplayName("withId() 메서드로 ID를 설정한 새로운 StadiumTransport를 생성한다")
    void withId_Success() {
        // given
        StadiumTransport transport = StadiumTransport.create(
            stadium, TransportType.SUBWAY, "1호선 대전역", "지하철 설명", "팁"
        );
        Long id = 1L;

        // when
        StadiumTransport transportWithId = transport.withId(id);

        // then
        assertThat(transportWithId.getId()).isEqualTo(id);
        assertThat(transportWithId.getRoute()).isEqualTo(transport.getRoute());
        assertThat(transport.getId()).isNull(); // 원본은 변경되지 않음
    }

    @Test
    @DisplayName("fromPersistence()에서 transportType이 null이면 예외가 발생한다")
    void fromPersistence_NullTransportType_ThrowsException() {
        // given
        Long id = 1L;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // when & then
        assertThatThrownBy(() -> StadiumTransport.fromPersistence(
            id, stadium, null, "1호선 대전역", "지하철 설명", "팁",
            createdAt, updatedAt, null
        )).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("fromPersistence()에서 route가 null이면 예외가 발생한다")
    void fromPersistence_NullRoute_ThrowsException() {
        // given
        Long id = 1L;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // when & then
        assertThatThrownBy(() -> StadiumTransport.fromPersistence(
            id, stadium, TransportType.SUBWAY, null, "지하철 설명", "팁",
            createdAt, updatedAt, null
        )).isInstanceOf(NullPointerException.class);
    }
}


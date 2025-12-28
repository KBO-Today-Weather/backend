package kbo.today.domain.player;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import kbo.today.domain.team.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("BattingRecord 도메인 엔티티 단위 테스트")
class BattingRecordTest {

    private Team team;
    private Player player;

    @BeforeEach
    void setUp() {
        team = Team.create("한화 이글스", "대전", "logo.png");
        player = Player.create(team, "이정후", 51, Position.CENTER_FIELD);
    }

    @Test
    @DisplayName("create() 팩토리 메서드로 BattingRecord를 생성한다")
    void create_Success() {
        // when
        BattingRecord record = BattingRecord.create(player, 2024);

        // then
        assertThat(record).isNotNull();
        assertThat(record.getId()).isNull();
        assertThat(record.getPlayer()).isEqualTo(player);
        assertThat(record.getSeason()).isEqualTo(2024);
        assertThat(record.getGames()).isEqualTo(0);
        assertThat(record.getPlateAppearances()).isEqualTo(0);
        assertThat(record.getAtBats()).isEqualTo(0);
        assertThat(record.getHits()).isEqualTo(0);
    }

    @Test
    @DisplayName("getWalkRate()는 볼넷 비율을 계산한다")
    void getWalkRate_CalculatesCorrectly() {
        // given
        BattingRecord record = BattingRecord.create(player, 2024);
        record.updateStats(100, 450, 400, 120, 20, 5, 15, 80, 70, 50, 60, 10, 5, 3, 2);

        // when
        Double walkRate = record.getWalkRate();

        // then
        // walkRate = walks / plateAppearances = 50 / 450
        assertThat(walkRate).isEqualTo(50.0 / 450.0);
    }

    @Test
    @DisplayName("getWalkRate()는 plateAppearances가 0이면 0.0을 반환한다")
    void getWalkRate_ZeroPlateAppearances_ReturnsZero() {
        // given
        BattingRecord record = BattingRecord.create(player, 2024);

        // when
        Double walkRate = record.getWalkRate();

        // then
        assertThat(walkRate).isEqualTo(0.0);
    }

    @Test
    @DisplayName("getBattingAverage()는 타율을 계산한다")
    void getBattingAverage_CalculatesCorrectly() {
        // given
        BattingRecord record = BattingRecord.create(player, 2024);
        record.updateStats(100, 450, 400, 120, 20, 5, 15, 80, 70, 50, 60, 10, 5, 3, 2);

        // when
        Double battingAverage = record.getBattingAverage();

        // then
        assertThat(battingAverage).isEqualTo(0.3); // 120 / 400 = 0.3
    }

    @Test
    @DisplayName("getBattingAverage()는 atBats가 0이면 0.0을 반환한다")
    void getBattingAverage_ZeroAtBats_ReturnsZero() {
        // given
        BattingRecord record = BattingRecord.create(player, 2024);

        // when
        Double battingAverage = record.getBattingAverage();

        // then
        assertThat(battingAverage).isEqualTo(0.0);
    }

    @Test
    @DisplayName("getOnBasePercentage()는 출루율을 계산한다")
    void getOnBasePercentage_CalculatesCorrectly() {
        // given
        BattingRecord record = BattingRecord.create(player, 2024);
        record.updateStats(100, 450, 400, 120, 20, 5, 15, 80, 70, 50, 60, 10, 5, 3, 2);

        // when
        Double obp = record.getOnBasePercentage();

        // then
        assertThat(obp).isEqualTo((120.0 + 50.0) / 450.0); // (hits + walks) / plateAppearances
    }

    @Test
    @DisplayName("getSluggingPercentage()는 장타율을 계산한다")
    void getSluggingPercentage_CalculatesCorrectly() {
        // given
        BattingRecord record = BattingRecord.create(player, 2024);
        record.updateStats(100, 450, 400, 120, 20, 5, 15, 80, 70, 50, 60, 10, 5, 3, 2);

        // when
        Double slg = record.getSluggingPercentage();

        // then
        // totalBases = 120 + 20 + (5 * 2) + (15 * 3) = 120 + 20 + 10 + 45 = 195
        // slg = 195 / 400 = 0.4875
        assertThat(slg).isEqualTo(195.0 / 400.0);
    }

    @Test
    @DisplayName("getOps()는 OPS를 계산한다")
    void getOps_CalculatesCorrectly() {
        // given
        BattingRecord record = BattingRecord.create(player, 2024);
        record.updateStats(100, 450, 400, 120, 20, 5, 15, 80, 70, 50, 60, 10, 5, 3, 2);

        // when
        Double ops = record.getOps();

        // then
        assertThat(ops).isEqualTo(record.getOnBasePercentage() + record.getSluggingPercentage());
    }

    @Test
    @DisplayName("updateStats() 메서드로 통계를 업데이트한다")
    void updateStats_Success() {
        // given
        BattingRecord record = BattingRecord.create(player, 2024);

        // when
        record.updateStats(100, 450, 400, 120, 20, 5, 15, 80, 70, 50, 60, 10, 5, 3, 2);

        // then
        assertThat(record.getGames()).isEqualTo(100);
        assertThat(record.getHits()).isEqualTo(120);
        assertThat(record.getHomeRuns()).isEqualTo(15);
    }

    @Test
    @DisplayName("create()에서 player가 null이면 예외가 발생한다")
    void create_NullPlayer_ThrowsException() {
        // when & then
        assertThatThrownBy(() -> BattingRecord.create(null, 2024))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("create()에서 season이 null이면 예외가 발생한다")
    void create_NullSeason_ThrowsException() {
        // when & then
        assertThatThrownBy(() -> BattingRecord.create(player, null))
            .isInstanceOf(NullPointerException.class);
    }
}


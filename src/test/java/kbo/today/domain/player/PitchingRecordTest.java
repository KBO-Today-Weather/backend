package kbo.today.domain.player;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import kbo.today.domain.team.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("PitchingRecord 도메인 엔티티 단위 테스트")
class PitchingRecordTest {

    private Team team;
    private Player player;

    @BeforeEach
    void setUp() {
        team = Team.create("한화 이글스", "대전", "logo.png");
        player = Player.create(team, "류현진", 99, Position.PITCHER);
    }

    @Test
    @DisplayName("create() 팩토리 메서드로 PitchingRecord를 생성한다")
    void create_Success() {
        // when
        PitchingRecord record = PitchingRecord.create(player, 2024);

        // then
        assertThat(record).isNotNull();
        assertThat(record.getId()).isNull();
        assertThat(record.getPlayer()).isEqualTo(player);
        assertThat(record.getSeason()).isEqualTo(2024);
        assertThat(record.getGames()).isEqualTo(0);
        assertThat(record.getWins()).isEqualTo(0);
        assertThat(record.getInningsPitched()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("getBabip()는 BABIP을 계산한다")
    void getBabip_CalculatesCorrectly() {
        // given
        PitchingRecord record = PitchingRecord.create(player, 2024);
        record.updateStats(30, 30, 15, 5, 0, 0, 200.0, 150, 60, 50, 40, 200, 10, 800, 5);

        // when
        Double babip = record.getBabip();

        // then
        // denominator = atBats - strikeouts - homeRuns + sacrificeFlies = 800 - 200 - 10 + 5 = 595
        // babip = (hits - homeRuns) / denominator = (150 - 10) / 595
        assertThat(babip).isEqualTo(140.0 / 595.0);
    }

    @Test
    @DisplayName("getBabip()는 denominator가 0 이하면 0.0을 반환한다")
    void getBabip_InvalidDenominator_ReturnsZero() {
        // given
        PitchingRecord record = PitchingRecord.create(player, 2024);
        record.updateStats(30, 30, 15, 5, 0, 0, 200.0, 0, 0, 0, 0, 200, 10, 0, 0);

        // when
        Double babip = record.getBabip();

        // then
        assertThat(babip).isEqualTo(0.0);
    }

    @Test
    @DisplayName("getEra()는 평균자책점을 계산한다")
    void getEra_CalculatesCorrectly() {
        // given
        PitchingRecord record = PitchingRecord.create(player, 2024);
        record.updateStats(30, 30, 15, 5, 0, 0, 200.0, 150, 60, 50, 40, 200, 10, 800, 5);

        // when
        Double era = record.getEra();

        // then
        // ERA = (earnedRuns * 9.0) / inningsPitched = (50 * 9.0) / 200.0 = 2.25
        assertThat(era).isEqualTo(2.25);
    }

    @Test
    @DisplayName("getEra()는 inningsPitched가 0이면 0.0을 반환한다")
    void getEra_ZeroInningsPitched_ReturnsZero() {
        // given
        PitchingRecord record = PitchingRecord.create(player, 2024);

        // when
        Double era = record.getEra();

        // then
        assertThat(era).isEqualTo(0.0);
    }

    @Test
    @DisplayName("getWhip()는 WHIP을 계산한다")
    void getWhip_CalculatesCorrectly() {
        // given
        PitchingRecord record = PitchingRecord.create(player, 2024);
        record.updateStats(30, 30, 15, 5, 0, 0, 200.0, 150, 60, 50, 40, 200, 10, 800, 5);

        // when
        Double whip = record.getWhip();

        // then
        // WHIP = (walks + hits) / inningsPitched = (40 + 150) / 200.0 = 0.95
        assertThat(whip).isEqualTo(0.95);
    }

    @Test
    @DisplayName("getWinningPercentage()는 승률을 계산한다")
    void getWinningPercentage_CalculatesCorrectly() {
        // given
        PitchingRecord record = PitchingRecord.create(player, 2024);
        record.updateStats(30, 30, 15, 5, 0, 0, 200.0, 150, 60, 50, 40, 200, 10, 800, 5);

        // when
        Double winningPercentage = record.getWinningPercentage();

        // then
        // winningPercentage = wins / (wins + losses) = 15 / (15 + 5) = 0.75
        assertThat(winningPercentage).isEqualTo(0.75);
    }

    @Test
    @DisplayName("getK9()는 9이닝당 탈삼진을 계산한다")
    void getK9_CalculatesCorrectly() {
        // given
        PitchingRecord record = PitchingRecord.create(player, 2024);
        record.updateStats(30, 30, 15, 5, 0, 0, 200.0, 150, 60, 50, 40, 200, 10, 800, 5);

        // when
        Double k9 = record.getK9();

        // then
        // K/9 = (strikeouts * 9.0) / inningsPitched = (200 * 9.0) / 200.0 = 9.0
        assertThat(k9).isEqualTo(9.0);
    }

    @Test
    @DisplayName("updateStats() 메서드로 통계를 업데이트한다")
    void updateStats_Success() {
        // given
        PitchingRecord record = PitchingRecord.create(player, 2024);

        // when
        record.updateStats(30, 30, 15, 5, 0, 0, 200.0, 150, 60, 50, 40, 200, 10, 800, 5);

        // then
        assertThat(record.getGames()).isEqualTo(30);
        assertThat(record.getWins()).isEqualTo(15);
        assertThat(record.getStrikeouts()).isEqualTo(200);
    }

    @Test
    @DisplayName("create()에서 player가 null이면 예외가 발생한다")
    void create_NullPlayer_ThrowsException() {
        // when & then
        assertThatThrownBy(() -> PitchingRecord.create(null, 2024))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("create()에서 season이 null이면 예외가 발생한다")
    void create_NullSeason_ThrowsException() {
        // when & then
        assertThatThrownBy(() -> PitchingRecord.create(player, null))
            .isInstanceOf(NullPointerException.class);
    }
}


package kbo.today.domain.player;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import kbo.today.domain.game.Game;
import kbo.today.domain.game.GameStatus;
import kbo.today.domain.team.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("GameBattingRecord 도메인 엔티티 단위 테스트")
class GameBattingRecordTest {

    private Team team;
    private Player player;
    private Game game;

    @BeforeEach
    void setUp() {
        team = Team.create("한화 이글스", "대전", "logo.png");
        player = Player.create(team, "이정후", 51, Position.CENTER_FIELD);
        Team awayTeam = Team.create("LG 트윈스", "서울", "logo2.png");
        game = Game.create(team, awayTeam, LocalDateTime.now(), "대전 한화생명 이글스파크");
    }

    @Test
    @DisplayName("create() 팩토리 메서드로 GameBattingRecord를 생성한다")
    void create_Success() {
        // when
        GameBattingRecord record = GameBattingRecord.create(player, game);

        // then
        assertThat(record).isNotNull();
        assertThat(record.getId()).isNull();
        assertThat(record.getPlayer()).isEqualTo(player);
        assertThat(record.getGame()).isEqualTo(game);
        assertThat(record.getPlateAppearances()).isEqualTo(0);
        assertThat(record.getAtBats()).isEqualTo(0);
        assertThat(record.getHits()).isEqualTo(0);
    }

    @Test
    @DisplayName("getBattingAverage()는 타율을 계산한다")
    void getBattingAverage_CalculatesCorrectly() {
        // given
        GameBattingRecord record = GameBattingRecord.create(player, game);
        // updateStats(plateAppearances, atBats, hits, doubles, triples, homeRuns, runs, rbis, walks, strikeouts, stolenBases, caughtStealing, sacrificeFlies, sacrificeBunts)
        record.updateStats(5, 4, 2, 0, 0, 0, 1, 1, 0, 1, 0, 0, 0, 0);

        // when
        Double battingAverage = record.getBattingAverage();

        // then
        assertThat(battingAverage).isEqualTo(0.5); // 2 / 4 = 0.5
    }

    @Test
    @DisplayName("getOnBasePercentage()는 출루율을 계산한다")
    void getOnBasePercentage_CalculatesCorrectly() {
        // given
        GameBattingRecord record = GameBattingRecord.create(player, game);
        // updateStats(plateAppearances, atBats, hits, doubles, triples, homeRuns, runs, rbis, walks, strikeouts, stolenBases, caughtStealing, sacrificeFlies, sacrificeBunts)
        record.updateStats(5, 4, 2, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0);

        // when
        Double obp = record.getOnBasePercentage();

        // then
        // obp = (hits + walks) / plateAppearances = (2 + 1) / 5 = 0.6
        assertThat(obp).isEqualTo(0.6);
    }

    @Test
    @DisplayName("getSluggingPercentage()는 장타율을 계산한다")
    void getSluggingPercentage_CalculatesCorrectly() {
        // given
        GameBattingRecord record = GameBattingRecord.create(player, game);
        // updateStats(plateAppearances, atBats, hits, doubles, triples, homeRuns, runs, rbis, walks, strikeouts, stolenBases, caughtStealing, sacrificeFlies, sacrificeBunts)
        record.updateStats(5, 4, 2, 1, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0);

        // when
        Double slg = record.getSluggingPercentage();

        // then
        // totalBases = 2 + 1 + (0 * 2) + (1 * 3) = 2 + 1 + 0 + 3 = 6
        // slg = 6 / 4 = 1.5
        assertThat(slg).isEqualTo(1.5);
    }

    @Test
    @DisplayName("getOps()는 OPS를 계산한다")
    void getOps_CalculatesCorrectly() {
        // given
        GameBattingRecord record = GameBattingRecord.create(player, game);
        // updateStats(plateAppearances, atBats, hits, doubles, triples, homeRuns, runs, rbis, walks, strikeouts, stolenBases, caughtStealing, sacrificeFlies, sacrificeBunts)
        record.updateStats(5, 4, 2, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0);

        // when
        Double ops = record.getOps();

        // then
        assertThat(ops).isEqualTo(record.getOnBasePercentage() + record.getSluggingPercentage());
    }

    @Test
    @DisplayName("getWalkRate()는 볼넷 비율을 계산한다")
    void getWalkRate_CalculatesCorrectly() {
        // given
        GameBattingRecord record = GameBattingRecord.create(player, game);
        // updateStats(plateAppearances, atBats, hits, doubles, triples, homeRuns, runs, rbis, walks, strikeouts, stolenBases, caughtStealing, sacrificeFlies, sacrificeBunts)
        record.updateStats(5, 4, 2, 0, 0, 0, 1, 1, 1, 1, 0, 0, 0, 0);

        // when
        Double walkRate = record.getWalkRate();

        // then
        // walkRate = walks / plateAppearances = 1 / 5 = 0.2
        assertThat(walkRate).isEqualTo(0.2);
    }

    @Test
    @DisplayName("updateStats() 메서드로 통계를 업데이트한다")
    void updateStats_Success() {
        // given
        GameBattingRecord record = GameBattingRecord.create(player, game);

        // when
        // updateStats(plateAppearances, atBats, hits, doubles, triples, homeRuns, runs, rbis, walks, strikeouts, stolenBases, caughtStealing, sacrificeFlies, sacrificeBunts)
        record.updateStats(5, 4, 2, 0, 0, 1, 1, 1, 0, 1, 0, 0, 0, 0);

        // then
        assertThat(record.getPlateAppearances()).isEqualTo(5);
        assertThat(record.getAtBats()).isEqualTo(4);
        assertThat(record.getHits()).isEqualTo(2);
        assertThat(record.getHomeRuns()).isEqualTo(1);
    }

    @Test
    @DisplayName("create()에서 player가 null이면 예외가 발생한다")
    void create_NullPlayer_ThrowsException() {
        // when & then
        assertThatThrownBy(() -> GameBattingRecord.create(null, game))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("create()에서 game이 null이면 예외가 발생한다")
    void create_NullGame_ThrowsException() {
        // when & then
        assertThatThrownBy(() -> GameBattingRecord.create(player, null))
            .isInstanceOf(NullPointerException.class);
    }
}


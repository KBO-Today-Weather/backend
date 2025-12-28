package kbo.today.domain.player;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import kbo.today.domain.game.Game;
import kbo.today.domain.team.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("GamePitchingRecord 도메인 엔티티 단위 테스트")
class GamePitchingRecordTest {

    private Team team;
    private Player player;
    private Game game;

    @BeforeEach
    void setUp() {
        team = Team.create("한화 이글스", "대전", "logo.png");
        player = Player.create(team, "류현진", 99, Position.PITCHER);
        Team awayTeam = Team.create("LG 트윈스", "서울", "logo2.png");
        game = Game.create(team, awayTeam, LocalDateTime.now(), "대전 한화생명 이글스파크");
    }

    @Test
    @DisplayName("create() 팩토리 메서드로 GamePitchingRecord를 생성한다")
    void create_Success() {
        // when
        GamePitchingRecord record = GamePitchingRecord.create(player, game);

        // then
        assertThat(record).isNotNull();
        assertThat(record.getId()).isNull();
        assertThat(record.getPlayer()).isEqualTo(player);
        assertThat(record.getGame()).isEqualTo(game);
        assertThat(record.getIsStarter()).isFalse();
        assertThat(record.getWins()).isEqualTo(0);
        assertThat(record.getInningsPitched()).isEqualTo(0.0);
    }

    @Test
    @DisplayName("getEra()는 평균자책점을 계산한다")
    void getEra_CalculatesCorrectly() {
        // given
        GamePitchingRecord record = GamePitchingRecord.create(player, game);
        record.updateStats(true, 1, 0, 0, 0, 9.0, 5, 2, 2, 1, 8, 0, 30, 0);

        // when
        Double era = record.getEra();

        // then
        // ERA = (earnedRuns * 9.0) / inningsPitched = (2 * 9.0) / 9.0 = 2.0
        assertThat(era).isEqualTo(2.0);
    }

    @Test
    @DisplayName("getWhip()는 WHIP을 계산한다")
    void getWhip_CalculatesCorrectly() {
        // given
        GamePitchingRecord record = GamePitchingRecord.create(player, game);
        record.updateStats(true, 1, 0, 0, 0, 9.0, 5, 2, 2, 1, 8, 0, 30, 0);

        // when
        Double whip = record.getWhip();

        // then
        // WHIP = (walks + hits) / inningsPitched = (1 + 5) / 9.0 = 0.666...
        assertThat(whip).isEqualTo(6.0 / 9.0);
    }

    @Test
    @DisplayName("getBabip()는 BABIP을 계산한다")
    void getBabip_CalculatesCorrectly() {
        // given
        GamePitchingRecord record = GamePitchingRecord.create(player, game);
        record.updateStats(true, 1, 0, 0, 0, 9.0, 5, 2, 2, 1, 8, 0, 30, 0);

        // when
        Double babip = record.getBabip();

        // then
        // denominator = atBats - strikeouts - homeRuns + sacrificeFlies = 30 - 8 - 0 + 0 = 22
        // babip = (hits - homeRuns) / denominator = (5 - 0) / 22
        assertThat(babip).isEqualTo(5.0 / 22.0);
    }

    @Test
    @DisplayName("getK9()는 9이닝당 탈삼진을 계산한다")
    void getK9_CalculatesCorrectly() {
        // given
        GamePitchingRecord record = GamePitchingRecord.create(player, game);
        record.updateStats(true, 1, 0, 0, 0, 9.0, 5, 2, 2, 1, 8, 0, 30, 0);

        // when
        Double k9 = record.getK9();

        // then
        // K/9 = (strikeouts * 9.0) / inningsPitched = (8 * 9.0) / 9.0 = 8.0
        assertThat(k9).isEqualTo(8.0);
    }

    @Test
    @DisplayName("updateStats() 메서드로 통계를 업데이트한다")
    void updateStats_Success() {
        // given
        GamePitchingRecord record = GamePitchingRecord.create(player, game);

        // when
        record.updateStats(true, 1, 0, 0, 0, 9.0, 5, 2, 2, 1, 8, 0, 30, 0);

        // then
        assertThat(record.getIsStarter()).isTrue();
        assertThat(record.getWins()).isEqualTo(1);
        assertThat(record.getInningsPitched()).isEqualTo(9.0);
        assertThat(record.getStrikeouts()).isEqualTo(8);
    }

    @Test
    @DisplayName("create()에서 player가 null이면 예외가 발생한다")
    void create_NullPlayer_ThrowsException() {
        // when & then
        assertThatThrownBy(() -> GamePitchingRecord.create(null, game))
            .isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("create()에서 game이 null이면 예외가 발생한다")
    void create_NullGame_ThrowsException() {
        // when & then
        assertThatThrownBy(() -> GamePitchingRecord.create(player, null))
            .isInstanceOf(NullPointerException.class);
    }
}


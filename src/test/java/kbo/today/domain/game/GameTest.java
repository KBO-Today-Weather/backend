package kbo.today.domain.game;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kbo.today.domain.player.GameBattingRecord;
import kbo.today.domain.player.GamePitchingRecord;
import kbo.today.domain.team.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Game 도메인 엔티티 단위 테스트")
class GameTest {

    private Team homeTeam;
    private Team awayTeam;
    private LocalDateTime gameDate;

    @BeforeEach
    void setUp() {
        homeTeam = Team.create("한화 이글스", "대전", "logo.png");
        awayTeam = Team.create("LG 트윈스", "서울", "logo2.png");
        gameDate = LocalDateTime.of(2024, 6, 15, 18, 30);
    }

    @Test
    @DisplayName("create() 팩토리 메서드로 Game을 생성한다")
    void create_Success() {
        // when
        Game game = Game.create(homeTeam, awayTeam, gameDate, "대전 한화생명 이글스파크");

        // then
        assertThat(game).isNotNull();
        assertThat(game.getHomeTeam()).isEqualTo(homeTeam);
        assertThat(game.getAwayTeam()).isEqualTo(awayTeam);
        assertThat(game.getGameDate()).isEqualTo(gameDate);
        assertThat(game.getStadium()).isEqualTo("대전 한화생명 이글스파크");
        assertThat(game.getHomeScore()).isNull();
        assertThat(game.getAwayScore()).isNull();
        assertThat(game.getStatus()).isEqualTo(GameStatus.SCHEDULED);
        assertThat(game.getBattingRecords()).isEmpty();
        assertThat(game.getPitchingRecords()).isEmpty();
        assertThat(game.getId()).isNull();
    }

    @Test
    @DisplayName("fromPersistence() 팩토리 메서드로 Game을 생성한다")
    void fromPersistence_Success() {
        // given
        Long id = 1L;
        Integer homeScore = 5;
        Integer awayScore = 3;
        GameStatus status = GameStatus.FINISHED;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();
        List<GameBattingRecord> battingRecords = new ArrayList<>();
        List<GamePitchingRecord> pitchingRecords = new ArrayList<>();

        // when
        Game game = Game.fromPersistence(
            id, homeTeam, awayTeam, gameDate, "대전 한화생명 이글스파크",
            homeScore, awayScore, status,
            battingRecords, pitchingRecords,
            createdAt, updatedAt, null
        );

        // then
        assertThat(game.getId()).isEqualTo(id);
        assertThat(game.getHomeScore()).isEqualTo(homeScore);
        assertThat(game.getAwayScore()).isEqualTo(awayScore);
        assertThat(game.getStatus()).isEqualTo(status);
        assertThat(game.getCreatedAt()).isEqualTo(createdAt);
        assertThat(game.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    @DisplayName("withId() 메서드로 ID를 설정한 새로운 Game을 생성한다")
    void withId_Success() {
        // given
        Game game = Game.create(homeTeam, awayTeam, gameDate, "대전 한화생명 이글스파크");
        Long id = 1L;

        // when
        Game gameWithId = game.withId(id);

        // then
        assertThat(gameWithId.getId()).isEqualTo(id);
        assertThat(gameWithId.getHomeTeam()).isEqualTo(game.getHomeTeam());
        assertThat(gameWithId.getAwayTeam()).isEqualTo(game.getAwayTeam());
        assertThat(gameWithId.getGameDate()).isEqualTo(game.getGameDate());
        assertThat(game.getId()).isNull(); // 원본은 변경되지 않음
    }

    @Test
    @DisplayName("updateScore() 메서드로 점수를 업데이트한다")
    void updateScore_Success() {
        // given
        Game game = Game.create(homeTeam, awayTeam, gameDate, "대전 한화생명 이글스파크");
        Integer newHomeScore = 7;
        Integer newAwayScore = 5;

        // when
        game.updateScore(newHomeScore, newAwayScore);

        // then
        assertThat(game.getHomeScore()).isEqualTo(newHomeScore);
        assertThat(game.getAwayScore()).isEqualTo(newAwayScore);
    }

    @Test
    @DisplayName("finish() 메서드로 경기를 종료한다")
    void finish_Success() {
        // given
        Game game = Game.create(homeTeam, awayTeam, gameDate, "대전 한화생명 이글스파크");
        assertThat(game.getStatus()).isEqualTo(GameStatus.SCHEDULED);

        // when
        game.finish();

        // then
        assertThat(game.getStatus()).isEqualTo(GameStatus.FINISHED);
    }

    @Test
    @DisplayName("fromPersistence()에서 status가 null이면 SCHEDULED로 설정된다")
    void fromPersistence_NullStatus_DefaultsToScheduled() {
        // given
        Long id = 1L;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // when
        Game game = Game.fromPersistence(
            id, homeTeam, awayTeam, gameDate, "대전 한화생명 이글스파크",
            null, null, null,
            new ArrayList<>(), new ArrayList<>(),
            createdAt, updatedAt, null
        );

        // then
        assertThat(game.getStatus()).isEqualTo(GameStatus.SCHEDULED);
    }

    @Test
    @DisplayName("fromPersistence()에서 battingRecords가 null이면 빈 리스트로 설정된다")
    void fromPersistence_NullBattingRecords_DefaultsToEmptyList() {
        // given
        Long id = 1L;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // when
        Game game = Game.fromPersistence(
            id, homeTeam, awayTeam, gameDate, "대전 한화생명 이글스파크",
            null, null, GameStatus.SCHEDULED,
            null, null,
            createdAt, updatedAt, null
        );

        // then
        assertThat(game.getBattingRecords()).isEmpty();
        assertThat(game.getPitchingRecords()).isEmpty();
    }

    @Test
    @DisplayName("fromPersistence()에서 homeTeam이 null이면 예외가 발생한다")
    void fromPersistence_NullHomeTeam_ThrowsException() {
        // given
        Long id = 1L;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // when & then
        assertThatThrownBy(() -> Game.fromPersistence(
            id, null, awayTeam, gameDate, "대전 한화생명 이글스파크",
            null, null, GameStatus.SCHEDULED,
            new ArrayList<>(), new ArrayList<>(),
            createdAt, updatedAt, null
        )).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("fromPersistence()에서 awayTeam이 null이면 예외가 발생한다")
    void fromPersistence_NullAwayTeam_ThrowsException() {
        // given
        Long id = 1L;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // when & then
        assertThatThrownBy(() -> Game.fromPersistence(
            id, homeTeam, null, gameDate, "대전 한화생명 이글스파크",
            null, null, GameStatus.SCHEDULED,
            new ArrayList<>(), new ArrayList<>(),
            createdAt, updatedAt, null
        )).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("fromPersistence()에서 gameDate가 null이면 예외가 발생한다")
    void fromPersistence_NullGameDate_ThrowsException() {
        // given
        Long id = 1L;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // when & then
        assertThatThrownBy(() -> Game.fromPersistence(
            id, homeTeam, awayTeam, null, "대전 한화생명 이글스파크",
            null, null, GameStatus.SCHEDULED,
            new ArrayList<>(), new ArrayList<>(),
            createdAt, updatedAt, null
        )).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("getBattingRecords()는 null이면 빈 리스트를 반환한다")
    void getBattingRecords_Null_ReturnsEmptyList() {
        // given
        Game game = Game.create(homeTeam, awayTeam, gameDate, "대전 한화생명 이글스파크");

        // when & then
        assertThat(game.getBattingRecords()).isEmpty();
    }

    @Test
    @DisplayName("getPitchingRecords()는 null이면 빈 리스트를 반환한다")
    void getPitchingRecords_Null_ReturnsEmptyList() {
        // given
        Game game = Game.create(homeTeam, awayTeam, gameDate, "대전 한화생명 이글스파크");

        // when & then
        assertThat(game.getPitchingRecords()).isEmpty();
    }
}


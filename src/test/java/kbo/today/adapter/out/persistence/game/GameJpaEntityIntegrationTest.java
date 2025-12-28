package kbo.today.adapter.out.persistence.game;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import kbo.today.adapter.out.persistence.team.TeamJpaEntity;
import kbo.today.domain.game.Game;
import kbo.today.domain.game.GameStatus;
import kbo.today.domain.team.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@ActiveProfiles("test")
@Transactional
@DisplayName("GameJpaEntity 변환 로직 통합 테스트")
class GameJpaEntityIntegrationTest {

    @Autowired
    private GameJpaRepository gameJpaRepository;

    @Autowired
    private kbo.today.adapter.out.persistence.team.TeamJpaRepository teamJpaRepository;

    private Team homeTeam;
    private Team awayTeam;
    private TeamJpaEntity homeTeamEntity;
    private TeamJpaEntity awayTeamEntity;

    @BeforeEach
    void setUp() {
        // Team 생성 및 저장
        homeTeam = Team.create("한화 이글스", "대전", "logo.png");
        awayTeam = Team.create("LG 트윈스", "서울", "logo2.png");
        
        homeTeamEntity = kbo.today.adapter.out.persistence.team.TeamJpaEntity.from(homeTeam);
        awayTeamEntity = kbo.today.adapter.out.persistence.team.TeamJpaEntity.from(awayTeam);
        
        homeTeamEntity = teamJpaRepository.save(homeTeamEntity);
        awayTeamEntity = teamJpaRepository.save(awayTeamEntity);
        
        homeTeam = homeTeamEntity.toDomain();
        awayTeam = awayTeamEntity.toDomain();
    }

    @Test
    @DisplayName("Game 도메인 엔티티를 GameJpaEntity로 변환한다")
    void from_DomainToJpa_Success() {
        // given
        LocalDateTime gameDate = LocalDateTime.of(2024, 6, 15, 18, 30);
        Game game = Game.create(homeTeam, awayTeam, gameDate, "대전 한화생명 이글스파크");

        // when
        GameJpaEntity entity = GameJpaEntity.from(game, homeTeamEntity, awayTeamEntity);

        // then
        assertThat(entity).isNotNull();
        assertThat(entity.getHomeTeam()).isEqualTo(homeTeamEntity);
        assertThat(entity.getAwayTeam()).isEqualTo(awayTeamEntity);
        assertThat(entity.getGameDate()).isEqualTo(gameDate);
        assertThat(entity.getStadium()).isEqualTo("대전 한화생명 이글스파크");
        assertThat(entity.getStatus()).isEqualTo(GameStatus.SCHEDULED);
        assertThat(entity.getHomeScore()).isNull();
        assertThat(entity.getAwayScore()).isNull();
    }

    @Test
    @DisplayName("GameJpaEntity를 Game 도메인 엔티티로 변환한다")
    void toDomain_JpaToDomain_Success() {
        // given
        LocalDateTime gameDate = LocalDateTime.of(2024, 6, 15, 18, 30);
        GameJpaEntity entity = new GameJpaEntity(homeTeamEntity, awayTeamEntity, gameDate, "대전 한화생명 이글스파크");
        entity.setHomeScore(5);
        entity.setAwayScore(3);
        entity.setStatus(GameStatus.FINISHED);
        entity = gameJpaRepository.save(entity);

        // when
        Game game = entity.toDomain();

        // then
        assertThat(game).isNotNull();
        assertThat(game.getId()).isEqualTo(entity.getId());
        assertThat(game.getHomeTeam()).isNotNull();
        assertThat(game.getHomeTeam().getName()).isEqualTo("한화 이글스");
        assertThat(game.getAwayTeam()).isNotNull();
        assertThat(game.getAwayTeam().getName()).isEqualTo("LG 트윈스");
        assertThat(game.getGameDate()).isEqualTo(gameDate);
        assertThat(game.getStadium()).isEqualTo("대전 한화생명 이글스파크");
        assertThat(game.getHomeScore()).isEqualTo(5);
        assertThat(game.getAwayScore()).isEqualTo(3);
        assertThat(game.getStatus()).isEqualTo(GameStatus.FINISHED);
    }

    @Test
    @DisplayName("Game 도메인 엔티티를 저장하고 조회하여 변환이 정확한지 확인한다")
    void saveAndRetrieve_ConversionAccuracy_Success() {
        // given
        LocalDateTime gameDate = LocalDateTime.of(2024, 6, 15, 18, 30);
        Game originalGame = Game.create(homeTeam, awayTeam, gameDate, "대전 한화생명 이글스파크");
        originalGame.updateScore(7, 5);
        originalGame.finish();

        // when
        GameJpaEntity entity = GameJpaEntity.from(originalGame, homeTeamEntity, awayTeamEntity);
        entity = gameJpaRepository.save(entity);
        Game retrievedGame = entity.toDomain();

        // then
        assertThat(retrievedGame.getHomeTeam().getName()).isEqualTo(originalGame.getHomeTeam().getName());
        assertThat(retrievedGame.getAwayTeam().getName()).isEqualTo(originalGame.getAwayTeam().getName());
        assertThat(retrievedGame.getGameDate()).isEqualTo(originalGame.getGameDate());
        assertThat(retrievedGame.getStadium()).isEqualTo(originalGame.getStadium());
        assertThat(retrievedGame.getHomeScore()).isEqualTo(originalGame.getHomeScore());
        assertThat(retrievedGame.getAwayScore()).isEqualTo(originalGame.getAwayScore());
        assertThat(retrievedGame.getStatus()).isEqualTo(originalGame.getStatus());
    }

    @Test
    @DisplayName("Game 도메인 엔티티에 ID가 있으면 GameJpaEntity에도 설정된다")
    void from_WithId_SetsId() {
        // given
        LocalDateTime gameDate = LocalDateTime.of(2024, 6, 15, 18, 30);
        Game game = Game.create(homeTeam, awayTeam, gameDate, "대전 한화생명 이글스파크");
        Game gameWithId = game.withId(100L);

        // when
        GameJpaEntity entity = GameJpaEntity.from(gameWithId, homeTeamEntity, awayTeamEntity);

        // then
        assertThat(entity.getId()).isEqualTo(100L);
    }

    @Test
    @DisplayName("Game 도메인 엔티티에 createdAt, updatedAt이 있으면 GameJpaEntity에도 설정된다")
    void from_WithTimestamps_SetsTimestamps() {
        // given
        LocalDateTime gameDate = LocalDateTime.of(2024, 6, 15, 18, 30);
        LocalDateTime createdAt = LocalDateTime.of(2024, 1, 1, 0, 0);
        LocalDateTime updatedAt = LocalDateTime.of(2024, 1, 2, 0, 0);
        
        Game game = Game.fromPersistence(
            1L, homeTeam, awayTeam, gameDate, "대전 한화생명 이글스파크",
            null, null, GameStatus.SCHEDULED,
            null, null,
            createdAt, updatedAt, null
        );

        // when
        GameJpaEntity entity = GameJpaEntity.from(game, homeTeamEntity, awayTeamEntity);

        // then
        assertThat(entity.getCreatedAt()).isEqualTo(createdAt);
        assertThat(entity.getUpdatedAt()).isEqualTo(updatedAt);
    }
}


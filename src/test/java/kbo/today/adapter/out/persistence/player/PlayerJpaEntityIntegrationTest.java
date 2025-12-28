package kbo.today.adapter.out.persistence.player;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import kbo.today.adapter.out.persistence.team.TeamJpaEntity;
import kbo.today.domain.player.Player;
import kbo.today.domain.player.Position;
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
@DisplayName("PlayerJpaEntity 변환 로직 통합 테스트")
class PlayerJpaEntityIntegrationTest {

    @Autowired
    private PlayerJpaRepository playerJpaRepository;

    @Autowired
    private kbo.today.adapter.out.persistence.team.TeamJpaRepository teamJpaRepository;

    private Team team;
    private TeamJpaEntity teamEntity;

    @BeforeEach
    void setUp() {
        // Team 생성 및 저장
        team = Team.create("한화 이글스", "대전", "logo.png");
        teamEntity = kbo.today.adapter.out.persistence.team.TeamJpaEntity.from(team);
        teamEntity = teamJpaRepository.save(teamEntity);
        team = teamEntity.toDomain();
    }

    @Test
    @DisplayName("Player 도메인 엔티티를 PlayerJpaEntity로 변환한다")
    void from_DomainToJpa_Success() {
        // given
        Player player = Player.create(team, "이정후", 51, Position.CENTER_FIELD);

        // when
        PlayerJpaEntity entity = PlayerJpaEntity.from(player, teamEntity);

        // then
        assertThat(entity).isNotNull();
        assertThat(entity.getTeam()).isEqualTo(teamEntity);
        assertThat(entity.getName()).isEqualTo("이정후");
        assertThat(entity.getBackNumber()).isEqualTo(51);
        assertThat(entity.getPosition()).isEqualTo(Position.CENTER_FIELD);
        assertThat(entity.getBirthDate()).isNull();
        assertThat(entity.getHeight()).isNull();
        assertThat(entity.getWeight()).isNull();
    }

    @Test
    @DisplayName("PlayerJpaEntity를 Player 도메인 엔티티로 변환한다")
    void toDomain_JpaToDomain_Success() {
        // given
        PlayerJpaEntity entity = new PlayerJpaEntity(teamEntity, "이정후", 51, Position.CENTER_FIELD);
        entity.setBirthDate(LocalDate.of(1998, 8, 20));
        entity.setHeight(185);
        entity.setWeight(85);
        entity = playerJpaRepository.save(entity);

        // when
        Player player = entity.toDomain();

        // then
        assertThat(player).isNotNull();
        assertThat(player.getId()).isEqualTo(entity.getId());
        assertThat(player.getTeam()).isNotNull();
        assertThat(player.getTeam().getName()).isEqualTo("한화 이글스");
        assertThat(player.getName()).isEqualTo("이정후");
        assertThat(player.getBackNumber()).isEqualTo(51);
        assertThat(player.getPosition()).isEqualTo(Position.CENTER_FIELD);
        assertThat(player.getBirthDate()).isEqualTo(LocalDate.of(1998, 8, 20));
        assertThat(player.getHeight()).isEqualTo(185);
        assertThat(player.getWeight()).isEqualTo(85);
    }

    @Test
    @DisplayName("Player 도메인 엔티티를 저장하고 조회하여 변환이 정확한지 확인한다")
    void saveAndRetrieve_ConversionAccuracy_Success() {
        // given
        Player originalPlayer = Player.create(team, "이정후", 51, Position.CENTER_FIELD);
        originalPlayer.updatePhysicalInfo(
            LocalDate.of(1998, 8, 20),
            185,
            85
        );

        // when
        PlayerJpaEntity entity = PlayerJpaEntity.from(originalPlayer, teamEntity);
        entity = playerJpaRepository.save(entity);
        Player retrievedPlayer = entity.toDomain();

        // then
        assertThat(retrievedPlayer.getTeam().getName()).isEqualTo(originalPlayer.getTeam().getName());
        assertThat(retrievedPlayer.getName()).isEqualTo(originalPlayer.getName());
        assertThat(retrievedPlayer.getBackNumber()).isEqualTo(originalPlayer.getBackNumber());
        assertThat(retrievedPlayer.getPosition()).isEqualTo(originalPlayer.getPosition());
        assertThat(retrievedPlayer.getBirthDate()).isEqualTo(originalPlayer.getBirthDate());
        assertThat(retrievedPlayer.getHeight()).isEqualTo(originalPlayer.getHeight());
        assertThat(retrievedPlayer.getWeight()).isEqualTo(originalPlayer.getWeight());
    }

    @Test
    @DisplayName("Player 도메인 엔티티에 ID가 있으면 PlayerJpaEntity에도 설정된다")
    void from_WithId_SetsId() {
        // given
        Player player = Player.create(team, "이정후", 51, Position.CENTER_FIELD);
        Player playerWithId = player.withId(100L);

        // when
        PlayerJpaEntity entity = PlayerJpaEntity.from(playerWithId, teamEntity);

        // then
        assertThat(entity.getId()).isEqualTo(100L);
    }

    @Test
    @DisplayName("Player 도메인 엔티티에 createdAt, updatedAt이 있으면 PlayerJpaEntity에도 설정된다")
    void from_WithTimestamps_SetsTimestamps() {
        // given
        LocalDate birthDate = LocalDate.of(1998, 8, 20);
        java.time.LocalDateTime createdAt = java.time.LocalDateTime.of(2024, 1, 1, 0, 0);
        java.time.LocalDateTime updatedAt = java.time.LocalDateTime.of(2024, 1, 2, 0, 0);
        
        Player player = Player.fromPersistence(
            1L, team, "이정후", 51, Position.CENTER_FIELD,
            birthDate, 185, 85,
            null, null,
            null, null,
            createdAt, updatedAt, null
        );

        // when
        PlayerJpaEntity entity = PlayerJpaEntity.from(player, teamEntity);

        // then
        assertThat(entity.getCreatedAt()).isEqualTo(createdAt);
        assertThat(entity.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    @DisplayName("Player 도메인 엔티티의 changeTeam() 후 변환 시 새로운 팀 정보가 반영된다")
    void from_AfterChangeTeam_ReflectsNewTeam() {
        // given
        Player player = Player.create(team, "이정후", 51, Position.CENTER_FIELD);
        
        Team newTeam = Team.create("LG 트윈스", "서울", "logo2.png");
        TeamJpaEntity newTeamEntity = kbo.today.adapter.out.persistence.team.TeamJpaEntity.from(newTeam);
        newTeamEntity = teamJpaRepository.save(newTeamEntity);
        
        player.changeTeam(newTeam);

        // when
        PlayerJpaEntity entity = PlayerJpaEntity.from(player, newTeamEntity);

        // then
        assertThat(entity.getTeam()).isEqualTo(newTeamEntity);
        assertThat(entity.getTeam().getName()).isEqualTo("LG 트윈스");
    }
}


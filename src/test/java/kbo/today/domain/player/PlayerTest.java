package kbo.today.domain.player;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import kbo.today.domain.team.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Player 도메인 엔티티 단위 테스트")
class PlayerTest {

    private Team team;
    private String name;
    private Integer backNumber;
    private Position position;

    @BeforeEach
    void setUp() {
        team = Team.create("한화 이글스", "대전", "logo.png");
        name = "이정후";
        backNumber = 51;
        position = Position.CENTER_FIELD;
    }

    @Test
    @DisplayName("create() 팩토리 메서드로 Player를 생성한다")
    void create_Success() {
        // when
        Player player = Player.create(team, name, backNumber, position);

        // then
        assertThat(player).isNotNull();
        assertThat(player.getTeam()).isEqualTo(team);
        assertThat(player.getName()).isEqualTo(name);
        assertThat(player.getBackNumber()).isEqualTo(backNumber);
        assertThat(player.getPosition()).isEqualTo(position);
        assertThat(player.getBirthDate()).isNull();
        assertThat(player.getHeight()).isNull();
        assertThat(player.getWeight()).isNull();
        assertThat(player.getBattingRecords()).isEmpty();
        assertThat(player.getPitchingRecords()).isEmpty();
        assertThat(player.getGameBattingRecords()).isEmpty();
        assertThat(player.getGamePitchingRecords()).isEmpty();
        assertThat(player.getId()).isNull();
    }

    @Test
    @DisplayName("fromPersistence() 팩토리 메서드로 Player를 생성한다")
    void fromPersistence_Success() {
        // given
        Long id = 1L;
        LocalDate birthDate = LocalDate.of(1998, 8, 20);
        Integer height = 185;
        Integer weight = 85;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();
        List<BattingRecord> battingRecords = new ArrayList<>();
        List<PitchingRecord> pitchingRecords = new ArrayList<>();
        List<GameBattingRecord> gameBattingRecords = new ArrayList<>();
        List<GamePitchingRecord> gamePitchingRecords = new ArrayList<>();

        // when
        Player player = Player.fromPersistence(
            id, team, name, backNumber, position,
            birthDate, height, weight,
            battingRecords, pitchingRecords,
            gameBattingRecords, gamePitchingRecords,
            createdAt, updatedAt, null
        );

        // then
        assertThat(player.getId()).isEqualTo(id);
        assertThat(player.getBirthDate()).isEqualTo(birthDate);
        assertThat(player.getHeight()).isEqualTo(height);
        assertThat(player.getWeight()).isEqualTo(weight);
        assertThat(player.getCreatedAt()).isEqualTo(createdAt);
        assertThat(player.getUpdatedAt()).isEqualTo(updatedAt);
    }

    @Test
    @DisplayName("withId() 메서드로 ID를 설정한 새로운 Player를 생성한다")
    void withId_Success() {
        // given
        Player player = Player.create(team, name, backNumber, position);
        Long id = 1L;

        // when
        Player playerWithId = player.withId(id);

        // then
        assertThat(playerWithId.getId()).isEqualTo(id);
        assertThat(playerWithId.getTeam()).isEqualTo(player.getTeam());
        assertThat(playerWithId.getName()).isEqualTo(player.getName());
        assertThat(playerWithId.getBackNumber()).isEqualTo(player.getBackNumber());
        assertThat(player.getId()).isNull(); // 원본은 변경되지 않음
    }

    @Test
    @DisplayName("updatePhysicalInfo() 메서드로 신체 정보를 업데이트한다")
    void updatePhysicalInfo_Success() {
        // given
        Player player = Player.create(team, name, backNumber, position);
        LocalDate newBirthDate = LocalDate.of(1998, 8, 20);
        Integer newHeight = 185;
        Integer newWeight = 85;

        // when
        player.updatePhysicalInfo(newBirthDate, newHeight, newWeight);

        // then
        assertThat(player.getBirthDate()).isEqualTo(newBirthDate);
        assertThat(player.getHeight()).isEqualTo(newHeight);
        assertThat(player.getWeight()).isEqualTo(newWeight);
    }

    @Test
    @DisplayName("changeTeam() 메서드로 팀을 변경한다")
    void changeTeam_Success() {
        // given
        Player player = Player.create(team, name, backNumber, position);
        Team newTeam = Team.create("LG 트윈스", "서울", "logo2.png");

        // when
        player.changeTeam(newTeam);

        // then
        assertThat(player.getTeam()).isEqualTo(newTeam);
    }

    @Test
    @DisplayName("changeBackNumber() 메서드로 등번호를 변경한다")
    void changeBackNumber_Success() {
        // given
        Player player = Player.create(team, name, backNumber, position);
        Integer newBackNumber = 1;

        // when
        player.changeBackNumber(newBackNumber);

        // then
        assertThat(player.getBackNumber()).isEqualTo(newBackNumber);
    }

    @Test
    @DisplayName("fromPersistence()에서 name이 null이면 예외가 발생한다")
    void fromPersistence_NullName_ThrowsException() {
        // given
        Long id = 1L;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // when & then
        assertThatThrownBy(() -> Player.fromPersistence(
            id, team, null, backNumber, position,
            null, null, null,
            new ArrayList<>(), new ArrayList<>(),
            new ArrayList<>(), new ArrayList<>(),
            createdAt, updatedAt, null
        )).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("fromPersistence()에서 backNumber가 null이면 예외가 발생한다")
    void fromPersistence_NullBackNumber_ThrowsException() {
        // given
        Long id = 1L;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // when & then
        assertThatThrownBy(() -> Player.fromPersistence(
            id, team, name, null, position,
            null, null, null,
            new ArrayList<>(), new ArrayList<>(),
            new ArrayList<>(), new ArrayList<>(),
            createdAt, updatedAt, null
        )).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("fromPersistence()에서 position이 null이면 예외가 발생한다")
    void fromPersistence_NullPosition_ThrowsException() {
        // given
        Long id = 1L;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // when & then
        assertThatThrownBy(() -> Player.fromPersistence(
            id, team, name, backNumber, null,
            null, null, null,
            new ArrayList<>(), new ArrayList<>(),
            new ArrayList<>(), new ArrayList<>(),
            createdAt, updatedAt, null
        )).isInstanceOf(NullPointerException.class);
    }

    @Test
    @DisplayName("fromPersistence()에서 battingRecords가 null이면 빈 리스트로 설정된다")
    void fromPersistence_NullBattingRecords_DefaultsToEmptyList() {
        // given
        Long id = 1L;
        LocalDateTime createdAt = LocalDateTime.now();
        LocalDateTime updatedAt = LocalDateTime.now();

        // when
        Player player = Player.fromPersistence(
            id, team, name, backNumber, position,
            null, null, null,
            null, null,
            null, null,
            createdAt, updatedAt, null
        );

        // then
        assertThat(player.getBattingRecords()).isEmpty();
        assertThat(player.getPitchingRecords()).isEmpty();
        assertThat(player.getGameBattingRecords()).isEmpty();
        assertThat(player.getGamePitchingRecords()).isEmpty();
    }

    @Test
    @DisplayName("getBattingRecords()는 null이면 빈 리스트를 반환한다")
    void getBattingRecords_Null_ReturnsEmptyList() {
        // given
        Player player = Player.create(team, name, backNumber, position);

        // when & then
        assertThat(player.getBattingRecords()).isEmpty();
    }

    @Test
    @DisplayName("getPitchingRecords()는 null이면 빈 리스트를 반환한다")
    void getPitchingRecords_Null_ReturnsEmptyList() {
        // given
        Player player = Player.create(team, name, backNumber, position);

        // when & then
        assertThat(player.getPitchingRecords()).isEmpty();
    }

    @Test
    @DisplayName("getGameBattingRecords()는 null이면 빈 리스트를 반환한다")
    void getGameBattingRecords_Null_ReturnsEmptyList() {
        // given
        Player player = Player.create(team, name, backNumber, position);

        // when & then
        assertThat(player.getGameBattingRecords()).isEmpty();
    }

    @Test
    @DisplayName("getGamePitchingRecords()는 null이면 빈 리스트를 반환한다")
    void getGamePitchingRecords_Null_ReturnsEmptyList() {
        // given
        Player player = Player.create(team, name, backNumber, position);

        // when & then
        assertThat(player.getGamePitchingRecords()).isEmpty();
    }
}


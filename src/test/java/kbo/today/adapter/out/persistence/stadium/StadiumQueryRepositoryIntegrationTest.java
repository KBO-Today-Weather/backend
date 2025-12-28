package kbo.today.adapter.out.persistence.stadium;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.Optional;
import kbo.today.domain.stadium.Stadium;
import kbo.today.domain.team.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Import(StadiumQueryRepository.class)
@ActiveProfiles("test")
@Transactional
@DisplayName("StadiumQueryRepository 통합 테스트")
class StadiumQueryRepositoryIntegrationTest {

    @Autowired
    private StadiumQueryRepository stadiumQueryRepository;

    @Autowired
    private StadiumJpaRepository stadiumJpaRepository;

    @Autowired
    private kbo.today.adapter.out.persistence.team.TeamJpaRepository teamJpaRepository;

    private Team team;
    private StadiumJpaEntity stadiumEntity;

    @BeforeEach
    void setUp() {
        // Team 생성 및 저장
        team = Team.create("한화 이글스", "대전", "logo.png");
        kbo.today.adapter.out.persistence.team.TeamJpaEntity teamEntity = 
            kbo.today.adapter.out.persistence.team.TeamJpaEntity.from(team);
        teamEntity = teamJpaRepository.save(teamEntity);
        team = teamEntity.toDomain();

        // Stadium 생성 및 저장
        Stadium stadium = Stadium.create(team, "대전 한화생명 이글스파크", "대전광역시 중구", 20000, 36.3174, 127.4288);
        stadiumEntity = StadiumJpaEntity.from(stadium, teamEntity);
        stadiumEntity = stadiumJpaRepository.save(stadiumEntity);
    }

    @Test
    @DisplayName("findAllWithTeam()로 모든 구장을 팀 정보와 함께 조회한다")
    void findAllWithTeam_Success() {
        // when
        List<Stadium> stadiums = stadiumQueryRepository.findAllWithTeam();

        // then
        assertThat(stadiums).isNotNull();
        assertThat(stadiums).isNotEmpty();
        
        // 저장한 구장이 포함되어 있는지 확인
        Optional<Stadium> foundStadium = stadiums.stream()
            .filter(s -> s.getName().equals("대전 한화생명 이글스파크"))
            .findFirst();
        
        assertThat(foundStadium).isPresent();
        assertThat(foundStadium.get().getTeam()).isNotNull();
        assertThat(foundStadium.get().getTeam().getName()).isEqualTo("한화 이글스");
    }

    @Test
    @DisplayName("findByIdForWeather()로 날씨 조회용 구장 정보를 조회한다")
    void findByIdForWeather_Success() {
        // given
        Long stadiumId = stadiumEntity.getId();

        // when
        Optional<Stadium> result = stadiumQueryRepository.findByIdForWeather(stadiumId);

        // then
        assertThat(result).isPresent();
        Stadium stadium = result.get();
        assertThat(stadium.getId()).isEqualTo(stadiumId);
        assertThat(stadium.getName()).isEqualTo("대전 한화생명 이글스파크");
        assertThat(stadium.getLatitude()).isEqualTo(36.3174);
        assertThat(stadium.getLongitude()).isEqualTo(127.4288);
        assertThat(stadium.getTeam()).isNotNull();
    }

    @Test
    @DisplayName("findByIdForWeather()로 존재하지 않는 구장 ID 조회 시 빈 Optional을 반환한다")
    void findByIdForWeather_NotFound_ReturnsEmpty() {
        // when
        Optional<Stadium> result = stadiumQueryRepository.findByIdForWeather(999L);

        // then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("findByIdWithDetails()로 구장 상세 정보를 조회한다")
    void findByIdWithDetails_Success() {
        // given
        Long stadiumId = stadiumEntity.getId();

        // when
        Optional<Stadium> result = stadiumQueryRepository.findByIdWithDetails(stadiumId);

        // then
        assertThat(result).isPresent();
        Stadium stadium = result.get();
        assertThat(stadium.getId()).isEqualTo(stadiumId);
        assertThat(stadium.getName()).isEqualTo("대전 한화생명 이글스파크");
        assertThat(stadium.getTeam()).isNotNull();
        // foods, seats, transports는 빈 리스트일 수 있음
        assertThat(stadium.getFoods()).isNotNull();
        assertThat(stadium.getSeats()).isNotNull();
        assertThat(stadium.getTransports()).isNotNull();
    }

    @Test
    @DisplayName("findByIdWithDetails()로 존재하지 않는 구장 ID 조회 시 빈 Optional을 반환한다")
    void findByIdWithDetails_NotFound_ReturnsEmpty() {
        // when
        Optional<Stadium> result = stadiumQueryRepository.findByIdWithDetails(999L);

        // then
        assertThat(result).isEmpty();
    }
}


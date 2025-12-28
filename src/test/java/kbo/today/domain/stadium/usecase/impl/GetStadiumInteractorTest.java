package kbo.today.domain.stadium.usecase.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kbo.today.domain.stadium.Stadium;
import kbo.today.domain.stadium.port.StadiumRepositoryPort;
import kbo.today.domain.team.Team;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetStadiumInteractor 단위 테스트")
class GetStadiumInteractorTest {

    @Mock
    private StadiumRepositoryPort stadiumRepositoryPort;

    @InjectMocks
    private GetStadiumInteractor getStadiumInteractor;

    private Team team;
    private Stadium stadium1;
    private Stadium stadium2;

    @BeforeEach
    void setUp() {
        team = Team.create("한화 이글스", "대전", "logo.png");
        stadium1 = Stadium.create(team, "대전 한화생명 이글스파크", "대전광역시 중구", 20000);
        stadium2 = Stadium.create(team, "잠실야구장", "서울특별시 송파구", 25000);
    }

    @Test
    @DisplayName("모든 구장 목록을 조회한다")
    void getAll_Success() {
        // given
        List<Stadium> expectedStadiums = Arrays.asList(stadium1, stadium2);
        when(stadiumRepositoryPort.findAll()).thenReturn(expectedStadiums);

        // when
        List<Stadium> result = getStadiumInteractor.getAll();

        // then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactlyInAnyOrder(stadium1, stadium2);
        verify(stadiumRepositoryPort).findAll();
    }

    @Test
    @DisplayName("구장 ID로 구장을 조회한다")
    void getById_Success() {
        // given
        when(stadiumRepositoryPort.findById(1L)).thenReturn(Optional.of(stadium1));

        // when
        Optional<Stadium> result = getStadiumInteractor.getById(1L);

        // then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(stadium1);
        assertThat(result.get().getName()).isEqualTo("대전 한화생명 이글스파크");
        verify(stadiumRepositoryPort).findById(1L);
    }

    @Test
    @DisplayName("존재하지 않는 구장 ID로 조회 시 빈 Optional을 반환한다")
    void getById_NotFound_ReturnsEmpty() {
        // given
        when(stadiumRepositoryPort.findById(999L)).thenReturn(Optional.empty());

        // when
        Optional<Stadium> result = getStadiumInteractor.getById(999L);

        // then
        assertThat(result).isEmpty();
        verify(stadiumRepositoryPort).findById(999L);
    }
}


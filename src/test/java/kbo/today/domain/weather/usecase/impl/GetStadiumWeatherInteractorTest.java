package kbo.today.domain.weather.usecase.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;
import kbo.today.common.exception.InvalidStadiumLocationException;
import kbo.today.common.exception.StadiumNotFoundException;
import kbo.today.domain.stadium.Stadium;
import kbo.today.domain.stadium.port.StadiumRepositoryPort;
import kbo.today.domain.team.Team;
import kbo.today.domain.weather.WeatherForecast;
import kbo.today.domain.weather.port.WeatherApiPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("GetStadiumWeatherInteractor 단위 테스트")
class GetStadiumWeatherInteractorTest {

    @Mock
    private StadiumRepositoryPort stadiumRepository;

    @Mock
    private WeatherApiPort weatherApiPort;

    @InjectMocks
    private GetStadiumWeatherInteractor getStadiumWeatherInteractor;

    private Team team;

    @BeforeEach
    void setUp() {
        team = Team.create("한화 이글스", "대전", "logo.png");
    }

    @Test
    @DisplayName("구장 ID로 날씨 정보를 성공적으로 조회한다")
    void getByStadiumId_Success() {
        // given
        Long stadiumId = 1L;
        Double latitude = 36.3174;
        Double longitude = 127.4288;
        
        Stadium stadiumWithLocation = Stadium.fromPersistence(
            stadiumId,
            team,
            "대전 한화생명 이글스파크",
            "대전광역시 중구",
            20000,
            latitude,
            longitude,
            null, null, null,
            null, null, null
        );

        WeatherForecast expectedForecast = new WeatherForecast(
            latitude,
            longitude,
            "Asia/Seoul",
            new WeatherForecast.CurrentWeather(
                "2024-01-01T12:00",
                15.0,
                60.0,
                14.0,
                0,
                5.0,
                180,
                0.0,
                0
            ),
            null,
            null
        );

        when(stadiumRepository.findByIdForWeather(stadiumId))
            .thenReturn(Optional.of(stadiumWithLocation));
        when(weatherApiPort.getWeatherForecast(latitude, longitude))
            .thenReturn(expectedForecast);

        // when
        WeatherForecast result = getStadiumWeatherInteractor.getByStadiumId(stadiumId);

        // then
        assertThat(result).isNotNull();
        assertThat(result.latitude()).isEqualTo(latitude);
        assertThat(result.longitude()).isEqualTo(longitude);
        assertThat(result.current()).isNotNull();
        assertThat(result.current().temperature()).isEqualTo(15.0);

        verify(stadiumRepository).findByIdForWeather(stadiumId);
        verify(weatherApiPort).getWeatherForecast(latitude, longitude);
    }

    @Test
    @DisplayName("존재하지 않는 구장 ID로 조회 시 예외를 발생시킨다")
    void getByStadiumId_StadiumNotFound_ThrowsException() {
        // given
        Long stadiumId = 999L;
        when(stadiumRepository.findByIdForWeather(stadiumId))
            .thenReturn(Optional.empty());

        // when & then
        assertThatThrownBy(() -> getStadiumWeatherInteractor.getByStadiumId(stadiumId))
            .isInstanceOf(StadiumNotFoundException.class)
            .hasMessageContaining("Stadium not found: 999");

        verify(stadiumRepository).findByIdForWeather(stadiumId);
        verify(weatherApiPort, never()).getWeatherForecast(anyDouble(), anyDouble());
    }

    @Test
    @DisplayName("위도가 없는 구장 조회 시 예외를 발생시킨다")
    void getByStadiumId_LatitudeNull_ThrowsException() {
        // given
        Long stadiumId = 1L;
        Stadium stadiumWithoutLatitude = Stadium.fromPersistence(
            stadiumId,
            team,
            "대전 한화생명 이글스파크",
            "대전광역시 중구",
            20000,
            null, // latitude가 null
            127.4288,
            null, null, null,
            null, null, null
        );

        when(stadiumRepository.findByIdForWeather(stadiumId))
            .thenReturn(Optional.of(stadiumWithoutLatitude));

        // when & then
        assertThatThrownBy(() -> getStadiumWeatherInteractor.getByStadiumId(stadiumId))
            .isInstanceOf(InvalidStadiumLocationException.class)
            .hasMessageContaining("Stadium location not set: 1");

        verify(stadiumRepository).findByIdForWeather(stadiumId);
        verify(weatherApiPort, never()).getWeatherForecast(anyDouble(), anyDouble());
    }

    @Test
    @DisplayName("경도가 없는 구장 조회 시 예외를 발생시킨다")
    void getByStadiumId_LongitudeNull_ThrowsException() {
        // given
        Long stadiumId = 1L;
        Stadium stadiumWithoutLongitude = Stadium.fromPersistence(
            stadiumId,
            team,
            "대전 한화생명 이글스파크",
            "대전광역시 중구",
            20000,
            36.3174,
            null, // longitude가 null
            null, null, null,
            null, null, null
        );

        when(stadiumRepository.findByIdForWeather(stadiumId))
            .thenReturn(Optional.of(stadiumWithoutLongitude));

        // when & then
        assertThatThrownBy(() -> getStadiumWeatherInteractor.getByStadiumId(stadiumId))
            .isInstanceOf(InvalidStadiumLocationException.class)
            .hasMessageContaining("Stadium location not set: 1");

        verify(stadiumRepository).findByIdForWeather(stadiumId);
        verify(weatherApiPort, never()).getWeatherForecast(anyDouble(), anyDouble());
    }
}


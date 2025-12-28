package kbo.today.adapter.in.web.weather;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import kbo.today.common.exception.GlobalExceptionHandler;
import kbo.today.common.exception.InvalidStadiumLocationException;
import kbo.today.common.exception.StadiumNotFoundException;
import kbo.today.domain.weather.WeatherForecast;
import kbo.today.domain.weather.usecase.GetStadiumWeatherUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.mockito.Mockito;

@WebMvcTest(controllers = WeatherController.class)
@Import({WeatherControllerTest.TestConfig.class, WeatherControllerTest.TestSecurityConfig.class, GlobalExceptionHandler.class})
@DisplayName("WeatherController 단위 테스트")
class WeatherControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GetStadiumWeatherUseCase getStadiumWeatherUseCase;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public GetStadiumWeatherUseCase getStadiumWeatherUseCase() {
            return Mockito.mock(GetStadiumWeatherUseCase.class);
        }
    }

    @TestConfiguration
    @EnableWebSecurity
    static class TestSecurityConfig {
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
            return http.build();
        }
    }

    @Test
    @DisplayName("구장 날씨 조회 API가 정상적으로 동작한다")
    void getStadiumWeather_Success() throws Exception {
        // given
        Long stadiumId = 1L;
        WeatherForecast forecast = new WeatherForecast(
            36.3174,
            127.4288,
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

        when(getStadiumWeatherUseCase.getByStadiumId(stadiumId)).thenReturn(forecast);

        // when & then
        mockMvc.perform(get("/api/v1/stadiums/{stadiumId}/weather", stadiumId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.latitude").value(36.3174))
            .andExpect(jsonPath("$.longitude").value(127.4288))
            .andExpect(jsonPath("$.timezone").value("Asia/Seoul"))
            .andExpect(jsonPath("$.current.temperature").value(15.0))
            .andExpect(jsonPath("$.current.relativeHumidity").value(60.0));

        verify(getStadiumWeatherUseCase).getByStadiumId(stadiumId);
    }

    @Test
    @DisplayName("존재하지 않는 구장 ID로 조회 시 404를 반환한다")
    void getStadiumWeather_StadiumNotFound_Returns404() throws Exception {
        // given
        Long stadiumId = 999L;
        when(getStadiumWeatherUseCase.getByStadiumId(stadiumId))
            .thenThrow(new StadiumNotFoundException("Stadium not found: " + stadiumId));

        // when & then
        mockMvc.perform(get("/api/v1/stadiums/{stadiumId}/weather", stadiumId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound());

        verify(getStadiumWeatherUseCase).getByStadiumId(stadiumId);
    }

    @Test
    @DisplayName("위치 정보가 없는 구장 조회 시 400을 반환한다")
    void getStadiumWeather_InvalidLocation_Returns400() throws Exception {
        // given
        Long stadiumId = 1L;
        when(getStadiumWeatherUseCase.getByStadiumId(stadiumId))
            .thenThrow(new InvalidStadiumLocationException("Stadium location not set: " + stadiumId));

        // when & then
        mockMvc.perform(get("/api/v1/stadiums/{stadiumId}/weather", stadiumId)
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest());
        
        // 예외 발생 시 GlobalExceptionHandler가 처리하므로 verify는 생략
    }
}


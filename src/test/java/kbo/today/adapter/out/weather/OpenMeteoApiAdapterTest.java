package kbo.today.adapter.out.weather;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import kbo.today.adapter.out.weather.dto.OpenMeteoCurrentData;
import kbo.today.adapter.out.weather.dto.OpenMeteoDailyData;
import kbo.today.adapter.out.weather.dto.OpenMeteoHourlyData;
import kbo.today.adapter.out.weather.dto.OpenMeteoResponse;
import kbo.today.common.exception.ErrorCode;
import kbo.today.common.exception.WeatherApiException;
import kbo.today.domain.weather.WeatherForecast;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@ExtendWith(MockitoExtension.class)
@DisplayName("OpenMeteoApiAdapter 단위 테스트")
class OpenMeteoApiAdapterTest {

    @Mock
    private WebClient webClient;

    @Mock
    private WebClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private WebClient.RequestHeadersSpec requestHeadersSpec;

    @Mock
    private WebClient.ResponseSpec responseSpec;

    @InjectMocks
    private OpenMeteoApiAdapter openMeteoApiAdapter;

    private OpenMeteoResponse mockResponse;

    @BeforeEach
    void setUp() {
        mockResponse = createMockResponse();
    }

    private void mockWebClientChain() {
        when(webClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(anyString())).thenReturn(requestHeadersSpec);
        when(requestHeadersSpec.retrieve()).thenReturn(responseSpec);
    }

    @Test
    @DisplayName("날씨 정보를 성공적으로 조회한다")
    void getWeatherForecast_Success() {
        // given
        Double latitude = 36.3174;
        Double longitude = 127.4288;
        mockWebClientChain();
        when(responseSpec.bodyToMono(eq(OpenMeteoResponse.class))).thenReturn(Mono.just(mockResponse));

        // when
        WeatherForecast result = openMeteoApiAdapter.getWeatherForecast(latitude, longitude).block();

        // then
        assertThat(result).isNotNull();
        assertThat(result.latitude()).isEqualTo(36.3174);
        assertThat(result.longitude()).isEqualTo(127.4288);
        assertThat(result.timezone()).isEqualTo("Asia/Seoul");
        assertThat(result.current()).isNotNull();
        assertThat(result.current().temperature()).isEqualTo(15.0);
        assertThat(result.current().relativeHumidity()).isEqualTo(60.0);
        assertThat(result.hourly()).isNotNull();
        assertThat(result.hourly()).hasSize(2);
        assertThat(result.daily()).isNotNull();
        assertThat(result.daily()).hasSize(2);
    }

    @Test
    @DisplayName("hourly와 daily 데이터가 null인 경우 빈 리스트로 반환한다")
    void getWeatherForecast_WithNullHourlyAndDaily_Success() {
        // given
        Double latitude = 36.3174;
        Double longitude = 127.4288;
        OpenMeteoResponse responseWithNull = createMockResponseWithNullLists();
        mockWebClientChain();
        when(responseSpec.bodyToMono(eq(OpenMeteoResponse.class))).thenReturn(Mono.just(responseWithNull));

        // when
        WeatherForecast result = openMeteoApiAdapter.getWeatherForecast(latitude, longitude).block();

        // then
        assertThat(result).isNotNull();
        assertThat(result.current()).isNotNull();
        assertThat(result.hourly()).isEmpty();
        assertThat(result.daily()).isEmpty();
    }

    @Test
    @DisplayName("API 응답이 null인 경우 RuntimeException을 발생시킨다")
    void getWeatherForecast_NullResponse_ThrowsException() {
        // given
        Double latitude = 36.3174;
        Double longitude = 127.4288;
        mockWebClientChain();
        when(responseSpec.bodyToMono(eq(OpenMeteoResponse.class))).thenReturn(Mono.empty());

        // when & then
        assertThatThrownBy(() -> openMeteoApiAdapter.getWeatherForecast(latitude, longitude).block())
            .isInstanceOf(WeatherApiException.class)
            .hasMessageContaining("null response")
            .matches(e -> ((WeatherApiException) e).getErrorCode() == ErrorCode.WEATHER_FETCH_FAILED);
    }

    @Test
    @DisplayName("current 데이터가 null인 경우 RuntimeException을 발생시킨다")
    void getWeatherForecast_NullCurrent_ThrowsException() {
        // given
        Double latitude = 36.3174;
        Double longitude = 127.4288;
        OpenMeteoResponse responseWithNullCurrent = createMockResponseWithNullCurrent();
        mockWebClientChain();
        when(responseSpec.bodyToMono(eq(OpenMeteoResponse.class))).thenReturn(Mono.just(responseWithNullCurrent));

        // when & then
        assertThatThrownBy(() -> openMeteoApiAdapter.getWeatherForecast(latitude, longitude).block())
            .isInstanceOf(WeatherApiException.class)
            .hasMessageContaining("Current weather data is missing")
            .matches(e -> ((WeatherApiException) e).getErrorCode() == ErrorCode.WEATHER_INVALID_RESPONSE);
    }

    @Test
    @DisplayName("WebClientResponseException 발생 시 RuntimeException으로 래핑하여 발생시킨다")
    void getWeatherForecast_WebClientResponseException_ThrowsException() {
        // given
        Double latitude = 36.3174;
        Double longitude = 127.4288;
        mockWebClientChain();
        when(responseSpec.bodyToMono(eq(OpenMeteoResponse.class)))
            .thenReturn(Mono.error(WebClientResponseException.create(500, "Server Error", HttpHeaders.EMPTY, new byte[0], null)));

        // when & then
        assertThatThrownBy(() -> openMeteoApiAdapter.getWeatherForecast(latitude, longitude).block())
            .isInstanceOf(WeatherApiException.class)
            .hasMessageContaining("Failed to fetch weather data from Open-Meteo API")
            .matches(e -> ((WeatherApiException) e).getErrorCode() == ErrorCode.WEATHER_FETCH_FAILED);
    }

    @Test
    @DisplayName("예상치 못한 예외 발생 시 RuntimeException으로 래핑하여 발생시킨다")
    void getWeatherForecast_UnexpectedException_ThrowsException() {
        // given
        Double latitude = 36.3174;
        Double longitude = 127.4288;
        mockWebClientChain();
        when(responseSpec.bodyToMono(eq(OpenMeteoResponse.class)))
            .thenReturn(Mono.error(new RuntimeException("Unexpected error")));

        // when & then
        assertThatThrownBy(() -> openMeteoApiAdapter.getWeatherForecast(latitude, longitude).block())
            .isInstanceOf(WeatherApiException.class)
            .hasMessageContaining("Failed to process weather data")
            .matches(e -> ((WeatherApiException) e).getErrorCode() == ErrorCode.WEATHER_INVALID_RESPONSE);
    }

    private OpenMeteoResponse createMockResponse() {
        return new OpenMeteoResponse(
            36.3174,
            127.4288,
            "Asia/Seoul",
            createCurrentData(),
            createHourlyData(),
            createDailyData()
        );
    }

    private OpenMeteoCurrentData createCurrentData() {
        return new OpenMeteoCurrentData(
            "2024-01-01T12:00",
            15.0,
            60.0,
            14.0,
            0,
            5.0,
            180,
            0.0,
            0
        );
    }

    private OpenMeteoHourlyData createHourlyData() {
        return new OpenMeteoHourlyData(
            Arrays.asList("2024-01-01T12:00", "2024-01-01T13:00"),
            Arrays.asList(15.0, 16.0),
            Arrays.asList(60.0, 65.0),
            Arrays.asList(0.0, 0.0),
            Arrays.asList(0, 0),
            Arrays.asList(0, 0),
            Arrays.asList(5.0, 6.0),
            Arrays.asList(180, 190)
        );
    }

    private OpenMeteoDailyData createDailyData() {
        return new OpenMeteoDailyData(
            Arrays.asList("2024-01-01", "2024-01-02"),
            Arrays.asList(0, 0),
            Arrays.asList(20.0, 21.0),
            Arrays.asList(10.0, 11.0),
            Arrays.asList(0.0, 0.0),
            Arrays.asList(0, 0),
            Arrays.asList(8.0, 9.0)
        );
    }

    private OpenMeteoResponse createMockResponseWithNullLists() {
        return new OpenMeteoResponse(
            36.3174,
            127.4288,
            "Asia/Seoul",
            createCurrentData(),
            null,
            null
        );
    }

    private OpenMeteoResponse createMockResponseWithNullCurrent() {
        return new OpenMeteoResponse(
            36.3174,
            127.4288,
            "Asia/Seoul",
            null,
            null,
            null
        );
    }
}

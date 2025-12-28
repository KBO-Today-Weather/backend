package kbo.today.adapter.out.weather;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

import java.util.Arrays;
import kbo.today.domain.weather.WeatherForecast;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

@ExtendWith(MockitoExtension.class)
@DisplayName("OpenMeteoApiAdapter 단위 테스트")
class OpenMeteoApiAdapterTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private OpenMeteoApiAdapter openMeteoApiAdapter;

    private Object mockResponse;
    private Class<?> responseClass;
    private static final String OPEN_METEO_API_URL = "https://api.open-meteo.com/v1/forecast";

    @BeforeEach
    void setUp() throws Exception {
        // Mock response 생성 (리플렉션 사용)
        responseClass = Class.forName("kbo.today.adapter.out.weather.OpenMeteoApiAdapter$OpenMeteoResponse");
        mockResponse = createMockResponse();
    }

    private String buildExpectedUrl(Double latitude, Double longitude) {
        return String.format(
            "%s?latitude=%.4f&longitude=%.4f&current=temperature_2m,relative_humidity_2m,apparent_temperature,weather_code,wind_speed_10m,wind_direction_10m,precipitation,precipitation_probability&hourly=temperature_2m,relative_humidity_2m,precipitation,precipitation_probability,weather_code,wind_speed_10m,wind_direction_10m&daily=weather_code,temperature_2m_max,temperature_2m_min,precipitation_sum,precipitation_probability_max,wind_speed_10m_max&timezone=Asia/Seoul",
            OPEN_METEO_API_URL, latitude, longitude
        );
    }

    @Test
    @DisplayName("날씨 정보를 성공적으로 조회한다")
    void getWeatherForecast_Success() {
        // given
        Double latitude = 36.3174;
        Double longitude = 127.4288;

        String expectedUrl = buildExpectedUrl(latitude, longitude);
        doReturn(mockResponse).when(restTemplate).getForObject(eq(expectedUrl), eq(responseClass));

        // when
        WeatherForecast result = openMeteoApiAdapter.getWeatherForecast(latitude, longitude);

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

        Object responseWithNull = createMockResponseWithNullLists();
        String expectedUrl = buildExpectedUrl(latitude, longitude);
        doReturn(responseWithNull).when(restTemplate).getForObject(eq(expectedUrl), eq(responseClass));

        // when
        WeatherForecast result = openMeteoApiAdapter.getWeatherForecast(latitude, longitude);

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

        String expectedUrl = buildExpectedUrl(latitude, longitude);
        doReturn(null).when(restTemplate).getForObject(eq(expectedUrl), eq(responseClass));

        // when & then
        assertThatThrownBy(() -> openMeteoApiAdapter.getWeatherForecast(latitude, longitude))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("null response");
    }

    @Test
    @DisplayName("current 데이터가 null인 경우 RuntimeException을 발생시킨다")
    void getWeatherForecast_NullCurrent_ThrowsException() {
        // given
        Double latitude = 36.3174;
        Double longitude = 127.4288;

        Object responseWithNullCurrent = createMockResponseWithNullCurrent();
        String expectedUrl = buildExpectedUrl(latitude, longitude);
        doReturn(responseWithNullCurrent).when(restTemplate).getForObject(eq(expectedUrl), eq(responseClass));

        // when & then
        assertThatThrownBy(() -> openMeteoApiAdapter.getWeatherForecast(latitude, longitude))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Current weather data is missing");
    }

    @Test
    @DisplayName("RestClientException 발생 시 RuntimeException으로 래핑하여 발생시킨다")
    void getWeatherForecast_RestClientException_ThrowsException() {
        // given
        Double latitude = 36.3174;
        Double longitude = 127.4288;

        String expectedUrl = buildExpectedUrl(latitude, longitude);
        doThrow(new RestClientException("Connection failed")).when(restTemplate).getForObject(eq(expectedUrl), eq(responseClass));

        // when & then
        assertThatThrownBy(() -> openMeteoApiAdapter.getWeatherForecast(latitude, longitude))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Failed to fetch weather data from Open-Meteo API");
    }

    @Test
    @DisplayName("예상치 못한 예외 발생 시 RuntimeException으로 래핑하여 발생시킨다")
    void getWeatherForecast_UnexpectedException_ThrowsException() {
        // given
        Double latitude = 36.3174;
        Double longitude = 127.4288;

        String expectedUrl = buildExpectedUrl(latitude, longitude);
        doThrow(new RuntimeException("Unexpected error")).when(restTemplate).getForObject(eq(expectedUrl), eq(responseClass));

        // when & then
        assertThatThrownBy(() -> openMeteoApiAdapter.getWeatherForecast(latitude, longitude))
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining("Failed to process weather data");
    }

    // Helper methods to create mock responses using reflection
    private Object createMockResponse() {
        try {
            // 리플렉션을 사용하여 내부 클래스 인스턴스 생성
            Class<?> responseClass = Class.forName("kbo.today.adapter.out.weather.OpenMeteoApiAdapter$OpenMeteoResponse");
            java.lang.reflect.Constructor<?> constructor = responseClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            Object response = constructor.newInstance();
            
            setField(response, "latitude", 36.3174);
            setField(response, "longitude", 127.4288);
            setField(response, "timezone", "Asia/Seoul");
            setField(response, "current", createCurrentData());
            setField(response, "hourly", createHourlyData());
            setField(response, "daily", createDailyData());
            
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create mock response", e);
        }
    }

    private Object createCurrentData() {
        try {
            Class<?> currentClass = Class.forName("kbo.today.adapter.out.weather.OpenMeteoApiAdapter$CurrentData");
            java.lang.reflect.Constructor<?> constructor = currentClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            Object current = constructor.newInstance();
            setField(current, "time", "2024-01-01T12:00");
            setField(current, "temperature2m", 15.0);
            setField(current, "relativeHumidity2m", 60.0);
            setField(current, "apparentTemperature", 14.0);
            setField(current, "weatherCode", 0);
            setField(current, "windSpeed10m", 5.0);
            setField(current, "windDirection10m", 180);
            setField(current, "precipitation", 0.0);
            setField(current, "precipitationProbability", 0);
            return current;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create current data", e);
        }
    }

    private Object createHourlyData() {
        try {
            Class<?> hourlyClass = Class.forName("kbo.today.adapter.out.weather.OpenMeteoApiAdapter$HourlyData");
            java.lang.reflect.Constructor<?> constructor = hourlyClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            Object hourly = constructor.newInstance();
            setField(hourly, "time", Arrays.asList("2024-01-01T12:00", "2024-01-01T13:00"));
            setField(hourly, "temperature2m", Arrays.asList(15.0, 16.0));
            setField(hourly, "relativeHumidity2m", Arrays.asList(60.0, 65.0));
            setField(hourly, "precipitation", Arrays.asList(0.0, 0.0));
            setField(hourly, "precipitationProbability", Arrays.asList(0, 0));
            setField(hourly, "weatherCode", Arrays.asList(0, 0));
            setField(hourly, "windSpeed10m", Arrays.asList(5.0, 6.0));
            setField(hourly, "windDirection10m", Arrays.asList(180, 190));
            return hourly;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create hourly data", e);
        }
    }

    private Object createDailyData() {
        try {
            Class<?> dailyClass = Class.forName("kbo.today.adapter.out.weather.OpenMeteoApiAdapter$DailyData");
            java.lang.reflect.Constructor<?> constructor = dailyClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            Object daily = constructor.newInstance();
            setField(daily, "time", Arrays.asList("2024-01-01", "2024-01-02"));
            setField(daily, "weatherCode", Arrays.asList(0, 0));
            setField(daily, "temperature2mMax", Arrays.asList(20.0, 21.0));
            setField(daily, "temperature2mMin", Arrays.asList(10.0, 11.0));
            setField(daily, "precipitationSum", Arrays.asList(0.0, 0.0));
            setField(daily, "precipitationProbabilityMax", Arrays.asList(0, 0));
            setField(daily, "windSpeed10mMax", Arrays.asList(8.0, 9.0));
            return daily;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create daily data", e);
        }
    }

    private Object createMockResponseWithNullLists() {
        try {
            Class<?> responseClass = Class.forName("kbo.today.adapter.out.weather.OpenMeteoApiAdapter$OpenMeteoResponse");
            java.lang.reflect.Constructor<?> constructor = responseClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            Object response = constructor.newInstance();
            setField(response, "latitude", 36.3174);
            setField(response, "longitude", 127.4288);
            setField(response, "timezone", "Asia/Seoul");
            setField(response, "current", createCurrentData());
            setField(response, "hourly", null);
            setField(response, "daily", null);
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create mock response", e);
        }
    }

    private Object createMockResponseWithNullCurrent() {
        try {
            Class<?> responseClass = Class.forName("kbo.today.adapter.out.weather.OpenMeteoApiAdapter$OpenMeteoResponse");
            java.lang.reflect.Constructor<?> constructor = responseClass.getDeclaredConstructor();
            constructor.setAccessible(true);
            Object response = constructor.newInstance();
            setField(response, "latitude", 36.3174);
            setField(response, "longitude", 127.4288);
            setField(response, "timezone", "Asia/Seoul");
            setField(response, "current", null);
            setField(response, "hourly", null);
            setField(response, "daily", null);
            return response;
        } catch (Exception e) {
            throw new RuntimeException("Failed to create mock response", e);
        }
    }

    private void setField(Object target, String fieldName, Object value) {
        try {
            java.lang.reflect.Field field = target.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(target, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException("Failed to set field: " + fieldName, e);
        }
    }
}


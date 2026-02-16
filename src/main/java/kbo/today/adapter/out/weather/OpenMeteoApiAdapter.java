package kbo.today.adapter.out.weather;

import kbo.today.adapter.out.weather.dto.OpenMeteoCurrentData;
import kbo.today.adapter.out.weather.dto.OpenMeteoDailyData;
import kbo.today.adapter.out.weather.dto.OpenMeteoHourlyData;
import kbo.today.adapter.out.weather.dto.OpenMeteoResponse;
import kbo.today.common.exception.ErrorCode;
import kbo.today.common.exception.WeatherApiException;
import kbo.today.config.WeatherCacheConfig;
import kbo.today.domain.weather.WeatherForecast;
import kbo.today.domain.weather.port.WeatherApiPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class OpenMeteoApiAdapter implements WeatherApiPort {

    private static final Logger log = LoggerFactory.getLogger(OpenMeteoApiAdapter.class);
    private static final String OPEN_METEO_API_URL = "https://api.open-meteo.com/v1/forecast";
    private final WebClient webClient;

    public OpenMeteoApiAdapter(WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * 같은 좌표(구장)에 대한 동시 요청은 캐시된 Mono를 공유하므로, 실제 Open-Meteo 호출은 한 번만 발생합니다 (single-flight).
     */
    @Override
    @Cacheable(value = WeatherCacheConfig.WEATHER_CACHE_NAME, key = "T(java.lang.String).format('%.4f-%.4f', #latitude, #longitude)")
    public Mono<WeatherForecast> getWeatherForecast(Double latitude, Double longitude) {
        String url = String.format(
            "%s?latitude=%.4f&longitude=%.4f&current=temperature_2m,relative_humidity_2m,apparent_temperature,weather_code,wind_speed_10m,wind_direction_10m,precipitation,precipitation_probability&hourly=temperature_2m,relative_humidity_2m,precipitation,precipitation_probability,weather_code,wind_speed_10m,wind_direction_10m&daily=weather_code,temperature_2m_max,temperature_2m_min,precipitation_sum,precipitation_probability_max,wind_speed_10m_max&timezone=Asia/Seoul",
            OPEN_METEO_API_URL, latitude, longitude
        );

        log.debug("Fetching weather data from Open-Meteo API: {}", url);

        return webClient.get()
            .uri(url)
            .retrieve()
            .bodyToMono(OpenMeteoResponse.class)
            .switchIfEmpty(Mono.error(new WeatherApiException(ErrorCode.WEATHER_FETCH_FAILED, "Failed to fetch weather data from Open-Meteo API: null response")))
            .doOnNext(response -> log.debug("API Response - latitude: {}, longitude: {}, timezone: {}, current: {}",
                response.latitude(), response.longitude(), response.timezone(),
                response.current() != null ? "present" : "null"))
            .map(this::mapToWeatherForecast)
            .doOnSuccess(forecast -> log.debug("Successfully fetched weather data for latitude: {}, longitude: {}", latitude, longitude))
            .onErrorMap(e -> {
                if (e instanceof WebClientResponseException ex) {
                    log.error("Error calling Open-Meteo API: {}", ex.getMessage(), ex);
                    return new WeatherApiException(ErrorCode.WEATHER_FETCH_FAILED, "Failed to fetch weather data from Open-Meteo API: " + ex.getMessage(), ex);
                }
                if (e instanceof WeatherApiException) {
                    return e;
                }
                log.error("Unexpected error while fetching weather data: {}", e.getMessage(), e);
                return new WeatherApiException(ErrorCode.WEATHER_INVALID_RESPONSE, "Failed to process weather data: " + e.getMessage(), e);
            })
            .cache();
    }

    private WeatherForecast mapToWeatherForecast(OpenMeteoResponse response) {
        OpenMeteoCurrentData current = response.current();
        OpenMeteoHourlyData hourly = response.hourly();
        OpenMeteoDailyData daily = response.daily();

        if (current == null) {
            throw new WeatherApiException(ErrorCode.WEATHER_INVALID_RESPONSE, "Current weather data is missing from API response");
        }

        WeatherForecast.CurrentWeather currentWeather = new WeatherForecast.CurrentWeather(
            current.time(),
            current.temperature2m(),
            current.relativeHumidity2m(),
            current.apparentTemperature(),
            current.weatherCode(),
            current.windSpeed10m(),
            current.windDirection10m(),
            current.precipitation(),
            current.precipitationProbability()
        );

        List<WeatherForecast.HourlyWeather> hourlyWeatherList = new ArrayList<>();
        if (hourly != null && hourly.time() != null) {
            List<String> time = hourly.time();
            IntStream.range(0, time.size()).forEach(i -> {
                hourlyWeatherList.add(new WeatherForecast.HourlyWeather(
                    time.get(i),
                    hourly.temperature2m().get(i),
                    hourly.relativeHumidity2m().get(i),
                    hourly.precipitation().get(i),
                    hourly.precipitationProbability() != null && i < hourly.precipitationProbability().size()
                        ? hourly.precipitationProbability().get(i) : null,
                    hourly.weatherCode().get(i),
                    hourly.windSpeed10m().get(i),
                    hourly.windDirection10m().get(i)
                ));
            });
        }

        List<WeatherForecast.DailyWeather> dailyWeatherList = new ArrayList<>();
        if (daily != null && daily.time() != null) {
            List<String> time = daily.time();
            IntStream.range(0, time.size()).forEach(i -> {
                dailyWeatherList.add(new WeatherForecast.DailyWeather(
                    time.get(i),
                    daily.weatherCode().get(i),
                    daily.temperature2mMax().get(i),
                    daily.temperature2mMin().get(i),
                    daily.precipitationSum().get(i),
                    daily.precipitationProbabilityMax() != null && i < daily.precipitationProbabilityMax().size()
                        ? daily.precipitationProbabilityMax().get(i) : null,
                    daily.windSpeed10mMax().get(i)
                ));
            });
        }

        return new WeatherForecast(
            response.latitude(),
            response.longitude(),
            response.timezone(),
            currentWeather,
            hourlyWeatherList,
            dailyWeatherList
        );
    }
}

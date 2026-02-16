package kbo.today.domain.weather.port;

import kbo.today.domain.weather.WeatherForecast;
import reactor.core.publisher.Mono;

public interface WeatherApiPort {
    Mono<WeatherForecast> getWeatherForecast(Double latitude, Double longitude);
}


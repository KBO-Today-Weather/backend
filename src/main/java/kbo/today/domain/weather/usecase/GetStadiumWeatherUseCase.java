package kbo.today.domain.weather.usecase;

import kbo.today.domain.weather.WeatherForecast;
import reactor.core.publisher.Mono;

public interface GetStadiumWeatherUseCase {
    Mono<WeatherForecast> getByStadiumId(Long stadiumId);
}


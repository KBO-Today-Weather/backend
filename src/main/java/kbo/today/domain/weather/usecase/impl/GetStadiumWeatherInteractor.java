package kbo.today.domain.weather.usecase.impl;

import kbo.today.common.exception.InvalidStadiumLocationException;
import kbo.today.common.exception.StadiumNotFoundException;
import kbo.today.domain.stadium.Stadium;
import kbo.today.domain.stadium.port.StadiumRepositoryPort;
import kbo.today.domain.weather.WeatherForecast;
import kbo.today.domain.weather.port.WeatherApiPort;
import kbo.today.domain.weather.usecase.GetStadiumWeatherUseCase;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

public class GetStadiumWeatherInteractor implements GetStadiumWeatherUseCase {

    private final StadiumRepositoryPort stadiumRepository;
    private final WeatherApiPort weatherApiPort;

    public GetStadiumWeatherInteractor(
        StadiumRepositoryPort stadiumRepository,
        WeatherApiPort weatherApiPort
    ) {
        this.stadiumRepository = stadiumRepository;
        this.weatherApiPort = weatherApiPort;
    }

    @Override
    public Mono<WeatherForecast> getByStadiumId(Long stadiumId) {
        return Mono.fromCallable(() -> stadiumRepository.findByIdForWeather(stadiumId))
            .subscribeOn(Schedulers.boundedElastic())
            .flatMap(optional -> optional
                .map(Mono::just)
                .orElse(Mono.error(new StadiumNotFoundException("Stadium not found: " + stadiumId))))
            .flatMap(stadium -> {
                if (stadium.getLatitude() == null || stadium.getLongitude() == null) {
                    return Mono.error(new InvalidStadiumLocationException("Stadium location not set: " + stadiumId));
                }
                return weatherApiPort.getWeatherForecast(stadium.getLatitude(), stadium.getLongitude());
            });
    }
}


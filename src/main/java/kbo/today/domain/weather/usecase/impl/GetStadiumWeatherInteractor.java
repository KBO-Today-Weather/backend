package kbo.today.domain.weather.usecase.impl;

import kbo.today.common.exception.InvalidStadiumLocationException;
import kbo.today.common.exception.StadiumNotFoundException;
import kbo.today.domain.stadium.Stadium;
import kbo.today.domain.stadium.port.StadiumRepositoryPort;
import kbo.today.domain.weather.WeatherForecast;
import kbo.today.domain.weather.port.WeatherApiPort;
import kbo.today.domain.weather.usecase.GetStadiumWeatherUseCase;

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
    public WeatherForecast getByStadiumId(Long stadiumId) {
        Stadium stadium = stadiumRepository.findByIdForWeather(stadiumId)
            .orElseThrow(() -> new StadiumNotFoundException("Stadium not found: " + stadiumId));

        if (stadium.getLatitude() == null || stadium.getLongitude() == null) {
            throw new InvalidStadiumLocationException("Stadium location not set: " + stadiumId);
        }

        return weatherApiPort.getWeatherForecast(stadium.getLatitude(), stadium.getLongitude());
    }
}


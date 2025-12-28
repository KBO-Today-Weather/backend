package kbo.today.domain.weather.usecase;

import kbo.today.domain.weather.WeatherForecast;

public interface GetStadiumWeatherUseCase {
    WeatherForecast getByStadiumId(Long stadiumId);
}


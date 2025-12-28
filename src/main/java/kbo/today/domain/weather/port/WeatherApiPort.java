package kbo.today.domain.weather.port;

import kbo.today.domain.weather.WeatherForecast;

public interface WeatherApiPort {
    WeatherForecast getWeatherForecast(Double latitude, Double longitude);
}


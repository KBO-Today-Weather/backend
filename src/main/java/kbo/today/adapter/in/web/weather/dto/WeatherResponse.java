package kbo.today.adapter.in.web.weather.dto;

import java.util.List;
import kbo.today.domain.weather.WeatherForecast;

public record WeatherResponse(
    Double latitude,
    Double longitude,
    String timezone,
    CurrentWeatherResponse current,
    List<HourlyWeatherResponse> hourly,
    List<DailyWeatherResponse> daily
) {
    public static WeatherResponse from(WeatherForecast forecast) {
        return new WeatherResponse(
            forecast.latitude(),
            forecast.longitude(),
            forecast.timezone(),
            CurrentWeatherResponse.from(forecast.current()),
            forecast.hourly() != null ? forecast.hourly().stream().map(HourlyWeatherResponse::from).toList() : null,
            forecast.daily() != null ? forecast.daily().stream().map(DailyWeatherResponse::from).toList() : null
        );
    }

    public record CurrentWeatherResponse(
        String time,
        Double temperature,
        Double relativeHumidity,
        Double apparentTemperature,
        Integer weatherCode,
        Double windSpeed,
        Integer windDirection,
        Double precipitation,
        Integer precipitationProbability
    ) {
        public static CurrentWeatherResponse from(WeatherForecast.CurrentWeather current) {
            return new CurrentWeatherResponse(
                current.time(),
                current.temperature(),
                current.relativeHumidity(),
                current.apparentTemperature(),
                current.weatherCode(),
                current.windSpeed(),
                current.windDirection(),
                current.precipitation(),
                current.precipitationProbability()
            );
        }
    }

    public record HourlyWeatherResponse(
        String time,
        Double temperature,
        Double relativeHumidity,
        Double precipitation,
        Integer precipitationProbability,
        Integer weatherCode,
        Double windSpeed,
        Integer windDirection
    ) {
        public static HourlyWeatherResponse from(WeatherForecast.HourlyWeather hourly) {
            return new HourlyWeatherResponse(
                hourly.time(),
                hourly.temperature(),
                hourly.relativeHumidity(),
                hourly.precipitation(),
                hourly.precipitationProbability(),
                hourly.weatherCode(),
                hourly.windSpeed(),
                hourly.windDirection()
            );
        }
    }

    public record DailyWeatherResponse(
        String time,
        Integer weatherCode,
        Double maxTemperature,
        Double minTemperature,
        Double precipitationSum,
        Integer precipitationProbabilityMax,
        Double windSpeedMax
    ) {
        public static DailyWeatherResponse from(WeatherForecast.DailyWeather daily) {
            return new DailyWeatherResponse(
                daily.time(),
                daily.weatherCode(),
                daily.maxTemperature(),
                daily.minTemperature(),
                daily.precipitationSum(),
                daily.precipitationProbabilityMax(),
                daily.windSpeedMax()
            );
        }
    }
}


package kbo.today.domain.weather;

import java.util.List;

public record WeatherForecast(
    Double latitude,
    Double longitude,
    String timezone,
    CurrentWeather current,
    List<HourlyWeather> hourly,
    List<DailyWeather> daily
) {
    public record CurrentWeather(
        String time,
        Double temperature,
        Double relativeHumidity,
        Double apparentTemperature,
        Integer weatherCode,
        Double windSpeed,
        Integer windDirection,
        Double precipitation
    ) {}
    
    public record HourlyWeather(
        String time,
        Double temperature,
        Double relativeHumidity,
        Double precipitation,
        Integer weatherCode,
        Double windSpeed,
        Integer windDirection
    ) {}
    
    public record DailyWeather(
        String time,
        Integer weatherCode,
        Double maxTemperature,
        Double minTemperature,
        Double precipitationSum,
        Double windSpeedMax
    ) {}
}


package kbo.today.adapter.out.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenMeteoHourlyData(
    @JsonProperty("time") List<String> time,
    @JsonProperty("temperature_2m") List<Double> temperature2m,
    @JsonProperty("relative_humidity_2m") List<Double> relativeHumidity2m,
    @JsonProperty("precipitation") List<Double> precipitation,
    @JsonProperty("precipitation_probability") List<Integer> precipitationProbability,
    @JsonProperty("weather_code") List<Integer> weatherCode,
    @JsonProperty("wind_speed_10m") List<Double> windSpeed10m,
    @JsonProperty("wind_direction_10m") List<Integer> windDirection10m
) {}

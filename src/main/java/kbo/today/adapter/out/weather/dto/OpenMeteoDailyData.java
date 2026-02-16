package kbo.today.adapter.out.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenMeteoDailyData(
    @JsonProperty("time") List<String> time,
    @JsonProperty("weather_code") List<Integer> weatherCode,
    @JsonProperty("temperature_2m_max") List<Double> temperature2mMax,
    @JsonProperty("temperature_2m_min") List<Double> temperature2mMin,
    @JsonProperty("precipitation_sum") List<Double> precipitationSum,
    @JsonProperty("precipitation_probability_max") List<Integer> precipitationProbabilityMax,
    @JsonProperty("wind_speed_10m_max") List<Double> windSpeed10mMax
) {}

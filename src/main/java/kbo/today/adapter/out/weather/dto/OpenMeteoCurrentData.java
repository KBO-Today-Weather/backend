package kbo.today.adapter.out.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenMeteoCurrentData(
    @JsonProperty("time") String time,
    @JsonProperty("temperature_2m") Double temperature2m,
    @JsonProperty("relative_humidity_2m") Double relativeHumidity2m,
    @JsonProperty("apparent_temperature") Double apparentTemperature,
    @JsonProperty("weather_code") Integer weatherCode,
    @JsonProperty("wind_speed_10m") Double windSpeed10m,
    @JsonProperty("wind_direction_10m") Integer windDirection10m,
    @JsonProperty("precipitation") Double precipitation,
    @JsonProperty("precipitation_probability") Integer precipitationProbability
) {}

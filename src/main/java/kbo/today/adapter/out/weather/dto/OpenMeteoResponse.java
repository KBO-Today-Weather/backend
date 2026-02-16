package kbo.today.adapter.out.weather.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record OpenMeteoResponse(
    @JsonProperty("latitude") Double latitude,
    @JsonProperty("longitude") Double longitude,
    @JsonProperty("timezone") String timezone,
    @JsonProperty("current") OpenMeteoCurrentData current,
    @JsonProperty("hourly") OpenMeteoHourlyData hourly,
    @JsonProperty("daily") OpenMeteoDailyData daily
) {}

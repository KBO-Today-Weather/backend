package kbo.today.adapter.out.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import kbo.today.domain.weather.WeatherForecast;
import kbo.today.domain.weather.port.WeatherApiPort;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class OpenMeteoApiAdapter implements WeatherApiPort {

    private static final String OPEN_METEO_API_URL = "https://api.open-meteo.com/v1/forecast";
    private final RestTemplate restTemplate;

    public OpenMeteoApiAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public WeatherForecast getWeatherForecast(Double latitude, Double longitude) {
        String url = String.format(
            "%s?latitude=%.4f&longitude=%.4f&current=temperature_2m,relative_humidity_2m,apparent_temperature,weather_code,wind_speed_10m,wind_direction_10m,precipitation&hourly=temperature_2m,relative_humidity_2m,precipitation,weather_code,wind_speed_10m,wind_direction_10m&daily=weather_code,temperature_2m_max,temperature_2m_min,precipitation_sum,wind_speed_10m_max&timezone=Asia/Seoul",
            OPEN_METEO_API_URL, latitude, longitude
        );

        OpenMeteoResponse response = restTemplate.getForObject(url, OpenMeteoResponse.class);
        
        if (response == null) {
            throw new RuntimeException("Failed to fetch weather data from Open-Meteo API");
        }

        return mapToWeatherForecast(response);
    }

    private WeatherForecast mapToWeatherForecast(OpenMeteoResponse response) {
        CurrentData current = response.current;
        HourlyData hourly = response.hourly;
        DailyData daily = response.daily;

        WeatherForecast.CurrentWeather currentWeather = new WeatherForecast.CurrentWeather(
            current.time,
            current.temperature2m,
            current.relativeHumidity2m,
            current.apparentTemperature,
            current.weatherCode,
            current.windSpeed10m,
            current.windDirection10m,
            current.precipitation
        );

        List<WeatherForecast.HourlyWeather> hourlyWeatherList = new ArrayList<>();
        if (hourly != null && hourly.time != null) {
            IntStream.range(0, hourly.time.size()).forEach(i -> {
                hourlyWeatherList.add(new WeatherForecast.HourlyWeather(
                    hourly.time.get(i),
                    hourly.temperature2m.get(i),
                    hourly.relativeHumidity2m.get(i),
                    hourly.precipitation.get(i),
                    hourly.weatherCode.get(i),
                    hourly.windSpeed10m.get(i),
                    hourly.windDirection10m.get(i)
                ));
            });
        }

        List<WeatherForecast.DailyWeather> dailyWeatherList = new ArrayList<>();
        if (daily != null && daily.time != null) {
            IntStream.range(0, daily.time.size()).forEach(i -> {
                dailyWeatherList.add(new WeatherForecast.DailyWeather(
                    daily.time.get(i),
                    daily.weatherCode.get(i),
                    daily.temperature2mMax.get(i),
                    daily.temperature2mMin.get(i),
                    daily.precipitationSum.get(i),
                    daily.windSpeed10mMax.get(i)
                ));
            });
        }

        return new WeatherForecast(
            response.latitude,
            response.longitude,
            response.timezone,
            currentWeather,
            hourlyWeatherList,
            dailyWeatherList
        );
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class OpenMeteoResponse {
        @JsonProperty("latitude")
        private Double latitude;
        
        @JsonProperty("longitude")
        private Double longitude;
        
        @JsonProperty("timezone")
        private String timezone;
        
        @JsonProperty("current")
        private CurrentData current;
        
        @JsonProperty("hourly")
        private HourlyData hourly;
        
        @JsonProperty("daily")
        private DailyData daily;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class CurrentData {
        @JsonProperty("time")
        private String time;
        
        @JsonProperty("temperature_2m")
        private Double temperature2m;
        
        @JsonProperty("relative_humidity_2m")
        private Double relativeHumidity2m;
        
        @JsonProperty("apparent_temperature")
        private Double apparentTemperature;
        
        @JsonProperty("weather_code")
        private Integer weatherCode;
        
        @JsonProperty("wind_speed_10m")
        private Double windSpeed10m;
        
        @JsonProperty("wind_direction_10m")
        private Integer windDirection10m;
        
        @JsonProperty("precipitation")
        private Double precipitation;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class HourlyData {
        @JsonProperty("time")
        private List<String> time;
        
        @JsonProperty("temperature_2m")
        private List<Double> temperature2m;
        
        @JsonProperty("relative_humidity_2m")
        private List<Double> relativeHumidity2m;
        
        @JsonProperty("precipitation")
        private List<Double> precipitation;
        
        @JsonProperty("weather_code")
        private List<Integer> weatherCode;
        
        @JsonProperty("wind_speed_10m")
        private List<Double> windSpeed10m;
        
        @JsonProperty("wind_direction_10m")
        private List<Integer> windDirection10m;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class DailyData {
        @JsonProperty("time")
        private List<String> time;
        
        @JsonProperty("weather_code")
        private List<Integer> weatherCode;
        
        @JsonProperty("temperature_2m_max")
        private List<Double> temperature2mMax;
        
        @JsonProperty("temperature_2m_min")
        private List<Double> temperature2mMin;
        
        @JsonProperty("precipitation_sum")
        private List<Double> precipitationSum;
        
        @JsonProperty("wind_speed_10m_max")
        private List<Double> windSpeed10mMax;
    }
}


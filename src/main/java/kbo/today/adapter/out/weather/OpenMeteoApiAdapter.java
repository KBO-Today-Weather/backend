package kbo.today.adapter.out.weather;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import kbo.today.domain.weather.WeatherForecast;
import kbo.today.domain.weather.port.WeatherApiPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

@Component
public class OpenMeteoApiAdapter implements WeatherApiPort {

    private static final Logger log = LoggerFactory.getLogger(OpenMeteoApiAdapter.class);
    private static final String OPEN_METEO_API_URL = "https://api.open-meteo.com/v1/forecast";
    private final RestTemplate restTemplate;

    public OpenMeteoApiAdapter(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public WeatherForecast getWeatherForecast(Double latitude, Double longitude) {
        String url = String.format(
            "%s?latitude=%.4f&longitude=%.4f&current=temperature_2m,relative_humidity_2m,apparent_temperature,weather_code,wind_speed_10m,wind_direction_10m,precipitation,precipitation_probability&hourly=temperature_2m,relative_humidity_2m,precipitation,precipitation_probability,weather_code,wind_speed_10m,wind_direction_10m&daily=weather_code,temperature_2m_max,temperature_2m_min,precipitation_sum,precipitation_probability_max,wind_speed_10m_max&timezone=Asia/Seoul",
            OPEN_METEO_API_URL, latitude, longitude
        );

        try {
            log.debug("Fetching weather data from Open-Meteo API: {}", url);
            OpenMeteoResponse response = restTemplate.getForObject(url, OpenMeteoResponse.class);
            
            if (response == null) {
                log.error("Open-Meteo API returned null response");
                throw new RuntimeException("Failed to fetch weather data from Open-Meteo API: null response");
            }

            log.debug("API Response - latitude: {}, longitude: {}, timezone: {}, current: {}", 
                response.latitude, response.longitude, response.timezone, 
                response.current != null ? "present" : "null");
            
            if (response.current == null) {
                log.error("Current weather data is null in API response");
            }

            log.debug("Successfully fetched weather data for latitude: {}, longitude: {}", latitude, longitude);
            return mapToWeatherForecast(response);
        } catch (RestClientException e) {
            log.error("Error calling Open-Meteo API: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to fetch weather data from Open-Meteo API: " + e.getMessage(), e);
        } catch (Exception e) {
            log.error("Unexpected error while fetching weather data: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to process weather data: " + e.getMessage(), e);
        }
    }

    private WeatherForecast mapToWeatherForecast(OpenMeteoResponse response) {
        CurrentData current = response.current;
        HourlyData hourly = response.hourly;
        DailyData daily = response.daily;

        if (current == null) {
            throw new RuntimeException("Current weather data is missing from API response");
        }

        WeatherForecast.CurrentWeather currentWeather = new WeatherForecast.CurrentWeather(
            current.time,
            current.temperature2m,
            current.relativeHumidity2m,
            current.apparentTemperature,
            current.weatherCode,
            current.windSpeed10m,
            current.windDirection10m,
            current.precipitation,
            current.precipitationProbability
        );

        List<WeatherForecast.HourlyWeather> hourlyWeatherList = new ArrayList<>();
        if (hourly != null && hourly.time != null) {
            IntStream.range(0, hourly.time.size()).forEach(i -> {
                hourlyWeatherList.add(new WeatherForecast.HourlyWeather(
                    hourly.time.get(i),
                    hourly.temperature2m.get(i),
                    hourly.relativeHumidity2m.get(i),
                    hourly.precipitation.get(i),
                    hourly.precipitationProbability != null && i < hourly.precipitationProbability.size() 
                        ? hourly.precipitationProbability.get(i) : null,
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
                    daily.precipitationProbabilityMax != null && i < daily.precipitationProbabilityMax.size()
                        ? daily.precipitationProbabilityMax.get(i) : null,
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

        // Getters for Jackson
        public Double getLatitude() { return latitude; }
        public Double getLongitude() { return longitude; }
        public String getTimezone() { return timezone; }
        public CurrentData getCurrent() { return current; }
        public HourlyData getHourly() { return hourly; }
        public DailyData getDaily() { return daily; }
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
        
        @JsonProperty("precipitation_probability")
        private Integer precipitationProbability;

        // Getters for Jackson
        public String getTime() { return time; }
        public Double getTemperature2m() { return temperature2m; }
        public Double getRelativeHumidity2m() { return relativeHumidity2m; }
        public Double getApparentTemperature() { return apparentTemperature; }
        public Integer getWeatherCode() { return weatherCode; }
        public Double getWindSpeed10m() { return windSpeed10m; }
        public Integer getWindDirection10m() { return windDirection10m; }
        public Double getPrecipitation() { return precipitation; }
        public Integer getPrecipitationProbability() { return precipitationProbability; }
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
        
        @JsonProperty("precipitation_probability")
        private List<Integer> precipitationProbability;
        
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
        
        @JsonProperty("precipitation_probability_max")
        private List<Integer> precipitationProbabilityMax;
        
        @JsonProperty("wind_speed_10m_max")
        private List<Double> windSpeed10mMax;
    }
}


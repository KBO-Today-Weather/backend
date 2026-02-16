package kbo.today.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "weather")
public record WeatherClientProperties(
    Cache cache,
    Webclient webclient
) {
    public record Cache(int maxSize, int expireMinutes) {}

    public record Webclient(int maxConnections, int connectTimeoutSeconds, int readTimeoutSeconds) {}
}

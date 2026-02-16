package kbo.today.config;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;
import reactor.netty.resources.ConnectionProvider;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Configuration
public class WebClientConfig {

    private static final String WEATHER_CONNECTION_PROVIDER_NAME = "weather-pool";
    private static final int DEFAULT_CONNECT_SECONDS = 3;
    private static final int DEFAULT_READ_SECONDS = 10;
    private static final int WRITE_TIMEOUT_SECONDS = 10;

    @Bean
    public WebClient webClient(WebClient.Builder builder, WeatherClientProperties properties) {
        int connectSeconds = properties.webclient() != null ? properties.webclient().connectTimeoutSeconds() : DEFAULT_CONNECT_SECONDS;
        int readSeconds = properties.webclient() != null ? properties.webclient().readTimeoutSeconds() : DEFAULT_READ_SECONDS;
        int maxConnections = properties.webclient() != null ? properties.webclient().maxConnections() : 500;
        if (connectSeconds <= 0) connectSeconds = DEFAULT_CONNECT_SECONDS;
        if (readSeconds <= 0) readSeconds = DEFAULT_READ_SECONDS;
        if (maxConnections <= 0) maxConnections = 500;

        final int connectMs = (int) TimeUnit.SECONDS.toMillis(connectSeconds);
        final int readSec = readSeconds;

        ConnectionProvider provider = ConnectionProvider.builder(WEATHER_CONNECTION_PROVIDER_NAME)
            .maxConnections(maxConnections)
            .pendingAcquireMaxCount(maxConnections * 2)
            .build();

        HttpClient httpClient = HttpClient.create(provider)
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, connectMs)
            .responseTimeout(Duration.ofSeconds(readSec))
            .doOnConnected(conn -> conn
                .addHandlerLast(new ReadTimeoutHandler(readSec, TimeUnit.SECONDS))
                .addHandlerLast(new WriteTimeoutHandler(WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)));

        return builder
            .clientConnector(new ReactorClientHttpConnector(httpClient))
            .build();
    }
}

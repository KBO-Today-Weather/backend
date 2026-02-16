package kbo.today.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import kbo.today.adapter.out.ratelimit.RateLimitEntry;
import kbo.today.adapter.out.ratelimit.RateLimitFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(RateLimitProperties.class)
public class RateLimitConfig {

    private final RateLimitProperties properties;

    @Bean
    public Cache<String, RateLimitEntry> rateLimitCache() {
        int windowSeconds = Math.max(
            properties.login().windowSeconds(),
            properties.signup().windowSeconds()
        );
        return Caffeine.newBuilder()
            .expireAfterWrite(windowSeconds * 2L, TimeUnit.SECONDS)
            .maximumSize(10_000)
            .build();
    }

    @Bean
    public RateLimitFilter rateLimitFilter(
        Cache<String, RateLimitEntry> rateLimitCache,
        RateLimitProperties rateLimitProperties,
        com.fasterxml.jackson.databind.ObjectMapper objectMapper
    ) {
        return new RateLimitFilter(rateLimitCache, rateLimitProperties, objectMapper);
    }
}

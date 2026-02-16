package kbo.today.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
@EnableConfigurationProperties(WeatherClientProperties.class)
public class WeatherCacheConfig {

    public static final String WEATHER_CACHE_NAME = "weather";
    private static final int DEFAULT_EXPIRE_MINUTES = 30;
    private static final int DEFAULT_MAX_SIZE = 500;

    @Bean
    public CacheManager cacheManager(WeatherClientProperties properties) {
        int expireMinutes = properties.cache() != null ? properties.cache().expireMinutes() : DEFAULT_EXPIRE_MINUTES;
        int maxSize = properties.cache() != null ? properties.cache().maxSize() : DEFAULT_MAX_SIZE;
        if (expireMinutes <= 0) expireMinutes = DEFAULT_EXPIRE_MINUTES;
        if (maxSize <= 0) maxSize = DEFAULT_MAX_SIZE;

        CaffeineCacheManager cacheManager = new CaffeineCacheManager(WEATHER_CACHE_NAME);
        cacheManager.setCaffeine(Caffeine.newBuilder()
            .expireAfterWrite(expireMinutes, TimeUnit.MINUTES)
            .maximumSize(maxSize));
        return cacheManager;
    }
}

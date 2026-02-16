package kbo.today.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class WeatherCacheConfig {

    public static final String WEATHER_CACHE_NAME = "weather";
    private static final int EXPIRE_MINUTES = 10;
    private static final int MAX_SIZE = 100;

    @Bean
    public CacheManager cacheManager() {
        CaffeineCacheManager cacheManager = new CaffeineCacheManager(WEATHER_CACHE_NAME);
        cacheManager.setCaffeine(Caffeine.newBuilder()
            .expireAfterWrite(EXPIRE_MINUTES, TimeUnit.MINUTES)
            .maximumSize(MAX_SIZE));
        return cacheManager;
    }
}

package kbo.today.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "rate-limit")
public record RateLimitProperties(
    Login login,
    Signup signup
) {
    public record Login(int maxRequests, int windowSeconds) {}

    public record Signup(int maxRequests, int windowSeconds) {}
}

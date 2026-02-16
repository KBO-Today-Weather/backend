package kbo.today.adapter.out.ratelimit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.benmanes.caffeine.cache.Cache;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kbo.today.common.exception.ErrorCode;
import kbo.today.common.exception.ErrorResponse;
import kbo.today.config.RateLimitProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public class RateLimitFilter extends OncePerRequestFilter {

    private static final String LOGIN_PATH = "/api/v1/auth/login";
    private static final String SIGNUP_PATH = "/api/v1/auth/signup";

    private final Cache<String, RateLimitEntry> cache;
    private final RateLimitProperties properties;
    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String path = request.getRequestURI();
        if (!isAuthPost(path, request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String clientIp = resolveClientIp(request);
        String cacheKey = path.contains("login") ? "login:" + clientIp : "signup:" + clientIp;

        int maxRequests;
        long windowMillis;
        if (path.endsWith("/login")) {
            maxRequests = properties.login().maxRequests();
            windowMillis = properties.login().windowSeconds() * 1000L;
        } else {
            maxRequests = properties.signup().maxRequests();
            windowMillis = properties.signup().windowSeconds() * 1000L;
        }

        RateLimitEntry entry = cache.get(cacheKey, k -> new RateLimitEntry());
        if (!entry.tryAcquire(windowMillis, maxRequests)) {
            writeRateLimitResponse(response);
            return;
        }

        filterChain.doFilter(request, response);
    }

    private boolean isAuthPost(String path, String method) {
        return "POST".equalsIgnoreCase(method)
            && path != null
            && (path.equals(LOGIN_PATH) || path.equals(SIGNUP_PATH));
    }

    private String resolveClientIp(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader("X-Forwarded-For"))
            .map(h -> h.split(",")[0].trim())
            .filter(s -> !s.isEmpty())
            .orElseGet(request::getRemoteAddr);
    }

    private void writeRateLimitResponse(HttpServletResponse response) throws IOException {
        response.setStatus(ErrorCode.RATE_LIMIT_EXCEEDED.getHttpStatus().value());
        response.setContentType("application/json;charset=UTF-8");
        ErrorResponse body = ErrorResponse.of(ErrorCode.RATE_LIMIT_EXCEEDED);
        objectMapper.writeValue(response.getOutputStream(), body);
    }
}

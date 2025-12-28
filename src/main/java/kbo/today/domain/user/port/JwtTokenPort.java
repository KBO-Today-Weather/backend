package kbo.today.domain.user.port;

public interface JwtTokenPort {
    String generateToken(Long userId, String email, String role);
}


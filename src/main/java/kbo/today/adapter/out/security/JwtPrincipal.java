package kbo.today.adapter.out.security;

import java.util.Objects;

public record JwtPrincipal(Long userId, String email, String role) {

    public JwtPrincipal {
        Objects.requireNonNull(userId, "userId must not be null");
        Objects.requireNonNull(email, "email must not be null");
        Objects.requireNonNull(role, "role must not be null");
    }
}

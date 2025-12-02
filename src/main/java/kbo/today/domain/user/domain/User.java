package kbo.today.domain.user.domain;

import java.util.Objects;
import kbo.today.domain.user.enumerable.UserRole;
import lombok.Getter;

@Getter
public class User {
    private final Long id;
    private final String email;
    private final String password;
    private final String nickname;
    private final UserRole role;

    public User(Long id, String email, String password, String nickname, UserRole role) {
        this.id = id;
        this.email = Objects.requireNonNull(email);
        this.password = Objects.requireNonNull(password);
        this.nickname = Objects.requireNonNull(nickname);
        this.role = Objects.requireNonNull(role);
    }

    public static User create(String email, String password, String nickname, UserRole role) {
        return new User(null, email, password, nickname, role);
    }

    public User withId(Long id) {
        return new User(id, this.email, this.password, this.nickname, this.role);
    }
}
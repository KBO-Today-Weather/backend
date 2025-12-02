package kbo.today.domain.user.usecase;

import kbo.today.domain.user.enumerable.UserRole;
import lombok.Getter;

@Getter
public class CreateUserCommand {
    private final String email;
    private final String password;
    private final String nickname;
    private final UserRole role;

    public CreateUserCommand(String email, String password, String nickname, UserRole role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
    }
}

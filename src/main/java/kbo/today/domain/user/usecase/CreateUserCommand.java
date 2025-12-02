package kbo.today.domain.user.usecase;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kbo.today.common.validation.SelfValidating;
import kbo.today.domain.user.enumerable.UserRole;
import lombok.Getter;

@Getter
public class CreateUserCommand extends SelfValidating<CreateUserCommand> {
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private final String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private final String password;

    @NotBlank(message = "Nickname is required")
    @Size(min = 2, max = 20, message = "Nickname must be between 2 and 20 characters")
    private final String nickname;

    @NotNull(message = "Role is required")
    private final UserRole role;

    public CreateUserCommand(String email, String password, String nickname, UserRole role) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.role = role;
        this.validateSelf();
    }
}

package kbo.today.domain.user.usecase;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import kbo.today.common.validation.SelfValidating;
import lombok.Getter;

@Getter
public class LoginCommand extends SelfValidating<LoginCommand> {
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private final String email;

    @NotBlank(message = "Password is required")
    private final String password;

    public LoginCommand(String email, String password) {
        this.email = email;
        this.password = password;
        this.validateSelf();
    }
}


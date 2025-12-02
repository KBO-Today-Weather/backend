package kbo.today.adapter.in.web.auth.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import kbo.today.common.validation.SelfValidating;
import lombok.Getter;

@Getter
public class LoginRequest extends SelfValidating<LoginRequest> {
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @NotBlank(message = "Password is required")
    private String password;

    public void validate() {
        validateSelf();
    }
}


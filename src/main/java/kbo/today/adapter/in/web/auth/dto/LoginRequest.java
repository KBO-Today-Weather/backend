package kbo.today.adapter.in.web.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import kbo.today.common.validation.SelfValidating;
import lombok.Getter;

@Getter
@Schema(description = "로그인 요청")
public class LoginRequest extends SelfValidating<LoginRequest> {
    @Schema(description = "이메일", example = "user@example.com", required = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @Schema(description = "비밀번호", example = "password123", required = true)
    @NotBlank(message = "Password is required")
    private String password;

    public void validate() {
        validateSelf();
    }
}


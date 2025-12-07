package kbo.today.adapter.in.web.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import kbo.today.common.validation.SelfValidating;
import lombok.Getter;

@Getter
@Schema(description = "회원가입 요청")
public class CreateUserRequest extends SelfValidating<CreateUserRequest> {
    @Schema(description = "이메일", example = "user@example.com", required = true)
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    private String email;

    @Schema(description = "비밀번호 (최소 8자)", example = "password123", required = true)
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    private String password;

    @Schema(description = "닉네임 (2-20자)", example = "nickname", required = true)
    @NotBlank(message = "Nickname is required")
    @Size(min = 2, max = 20, message = "Nickname must be between 2 and 20 characters")
    private String nickname;

    public void validate() {
        validateSelf();
    }
}


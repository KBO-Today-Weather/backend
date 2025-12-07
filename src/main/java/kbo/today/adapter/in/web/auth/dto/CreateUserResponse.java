package kbo.today.adapter.in.web.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "회원가입 응답")
public class CreateUserResponse {
    @Schema(description = "사용자 ID", example = "1")
    private final Long id;

    @Schema(description = "이메일", example = "user@example.com")
    private final String email;

    @Schema(description = "닉네임", example = "nickname")
    private final String nickname;

    @Schema(description = "응답 메시지", example = "User created successfully")
    private final String message;

    public CreateUserResponse(Long id, String email, String nickname) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.message = "User created successfully";
    }
}


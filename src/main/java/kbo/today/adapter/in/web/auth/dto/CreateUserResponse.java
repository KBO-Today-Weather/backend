package kbo.today.adapter.in.web.auth.dto;

import lombok.Getter;

@Getter
public class CreateUserResponse {
    private final Long id;
    private final String email;
    private final String nickname;
    private final String message;

    public CreateUserResponse(Long id, String email, String nickname) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.message = "User created successfully";
    }
}


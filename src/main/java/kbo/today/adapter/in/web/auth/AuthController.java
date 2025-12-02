package kbo.today.adapter.in.web.auth;

import kbo.today.adapter.in.web.auth.dto.CreateUserRequest;
import kbo.today.adapter.in.web.auth.dto.CreateUserResponse;
import kbo.today.adapter.in.web.auth.dto.LoginRequest;
import kbo.today.adapter.in.web.auth.dto.LoginResponse;
import kbo.today.domain.user.enumerable.UserRole;
import kbo.today.domain.user.usecase.CreateUserCommand;
import kbo.today.domain.user.usecase.CreateUserUseCase;
import kbo.today.domain.user.usecase.LoginCommand;
import kbo.today.domain.user.usecase.LoginUseCase;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final CreateUserUseCase createUserUseCase;

    public AuthController(LoginUseCase loginUseCase, CreateUserUseCase createUserUseCase) {
        this.loginUseCase = loginUseCase;
        this.createUserUseCase = createUserUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginCommand command = new LoginCommand(request.getEmail(), request.getPassword());
        String token = loginUseCase.login(command);
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<CreateUserResponse> signup(@Valid @RequestBody CreateUserRequest request) {
        UserRole role = (request.getRole() == null || request.getRole().isEmpty())
            ? UserRole.USER
            : UserRole.valueOf(request.getRole().toUpperCase());

        CreateUserCommand command = new CreateUserCommand(
            request.getEmail(),
            request.getPassword(),
            request.getNickname(),
            role
        );

        var createdUser = createUserUseCase.create(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            new CreateUserResponse(createdUser.getId(), createdUser.getEmail(), createdUser.getNickname())
        );
    }
}


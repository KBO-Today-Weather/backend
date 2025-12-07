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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Auth", description = "인증 관련 API")
@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    private final LoginUseCase loginUseCase;
    private final CreateUserUseCase createUserUseCase;

    public AuthController(LoginUseCase loginUseCase, CreateUserUseCase createUserUseCase) {
        this.loginUseCase = loginUseCase;
        this.createUserUseCase = createUserUseCase;
    }

    @Operation(summary = "로그인", description = "이메일과 비밀번호로 로그인하여 JWT 토큰을 발급받습니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "로그인 성공"),
        @ApiResponse(responseCode = "401", description = "인증 실패 (잘못된 이메일 또는 비밀번호)"),
        @ApiResponse(responseCode = "400", description = "입력값 검증 실패")
    })
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        LoginCommand command = new LoginCommand(request.getEmail(), request.getPassword());
        String token = loginUseCase.login(command);
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @Operation(summary = "회원가입", description = "새로운 사용자를 등록합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "회원가입 성공"),
        @ApiResponse(responseCode = "409", description = "이메일 중복"),
        @ApiResponse(responseCode = "400", description = "입력값 검증 실패")
    })
    @PostMapping("/signup")
    public ResponseEntity<CreateUserResponse> signup(@Valid @RequestBody CreateUserRequest request) {
        CreateUserCommand command = new CreateUserCommand(
            request.getEmail(),
            request.getPassword(),
            request.getNickname()
        );

        var createdUser = createUserUseCase.create(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(
            new CreateUserResponse(createdUser.getId(), createdUser.getEmail(), createdUser.getNickname())
        );
    }
}


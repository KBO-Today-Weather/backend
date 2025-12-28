package kbo.today.adapter.in.web.auth;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import kbo.today.adapter.in.web.auth.dto.CreateUserRequest;
import kbo.today.adapter.in.web.auth.dto.LoginRequest;
import kbo.today.adapter.in.web.auth.dto.TestAuthRequestFactory;
import kbo.today.domain.user.domain.User;
import kbo.today.domain.user.usecase.CreateUserCommand;
import kbo.today.domain.user.usecase.CreateUserUseCase;
import kbo.today.domain.user.usecase.LoginCommand;
import kbo.today.domain.user.usecase.LoginUseCase;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import org.mockito.Mockito;

@WebMvcTest(controllers = AuthController.class)
@Import(AuthControllerTest.TestSecurityConfig.class)
@DisplayName("AuthController 단위 테스트")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private LoginUseCase loginUseCase;

    @Autowired
    private CreateUserUseCase createUserUseCase;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public LoginUseCase loginUseCase() {
            return Mockito.mock(LoginUseCase.class);
        }

        @Bean
        public CreateUserUseCase createUserUseCase() {
            return Mockito.mock(CreateUserUseCase.class);
        }
    }

    @TestConfiguration
    @EnableWebSecurity
    static class TestSecurityConfig {
        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
            http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
            return http.build();
        }
    }

    @Test
    @DisplayName("회원가입 API가 정상적으로 동작한다")
    void signup_Success() throws Exception {
        // given
        CreateUserRequest request = TestAuthRequestFactory.createUserRequest("test@example.com", "password123", "테스트유저");
        User createdUser = new User(1L, "test@example.com", "encodedPassword", "테스트유저");

        given(createUserUseCase.create(any(CreateUserCommand.class)))
            .willReturn(createdUser);

        // when & then
        mockMvc.perform(post("/api/v1/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.email").value("test@example.com"))
            .andExpect(jsonPath("$.nickname").value("테스트유저"));
    }

    @Test
    @DisplayName("로그인 API가 정상적으로 동작한다")
    void login_Success() throws Exception {
        // given
        LoginRequest request = TestAuthRequestFactory.loginRequest("test@example.com", "password123");
        String token = "jwt.token.here";

        given(loginUseCase.login(any(LoginCommand.class)))
            .willReturn(token);

        // when & then
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").value(token));
    }

    @Test
    @DisplayName("회원가입 시 이메일 중복 시 409 에러를 반환한다")
    void signup_DuplicateEmail_Returns409() throws Exception {
        // given
        CreateUserRequest request = TestAuthRequestFactory.createUserRequest("test@example.com", "password123", "테스트유저");

        given(createUserUseCase.create(any(CreateUserCommand.class)))
            .willThrow(new kbo.today.common.exception.DuplicateEmailException("Email already exists"));

        // when & then
        mockMvc.perform(post("/api/v1/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("로그인 시 잘못된 자격증명으로 401 에러를 반환한다")
    void login_InvalidCredentials_Returns401() throws Exception {
        // given
        LoginRequest request = TestAuthRequestFactory.loginRequest("test@example.com", "wrongPassword");

        given(loginUseCase.login(any(LoginCommand.class)))
            .willThrow(new kbo.today.common.exception.InvalidCredentialsException());

        // when & then
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isUnauthorized());
    }
}


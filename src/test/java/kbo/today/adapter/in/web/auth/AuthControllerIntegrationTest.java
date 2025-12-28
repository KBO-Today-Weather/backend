package kbo.today.adapter.in.web.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import kbo.today.adapter.in.web.auth.dto.TestAuthRequestFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@DisplayName("AuthController 통합 테스트")
class AuthControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("회원가입 API가 실제 DB에 저장하며 정상적으로 동작한다")
    void signup_Integration_Success() throws Exception {
        // given
        var request = TestAuthRequestFactory.createUserRequest("test@example.com", "password123", "테스트유저");

        // when & then
        mockMvc.perform(post("/api/v1/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.email").value("test@example.com"))
            .andExpect(jsonPath("$.nickname").value("테스트유저"));
    }

    @Test
    @DisplayName("회원가입 후 로그인까지 전체 플로우가 정상적으로 동작한다")
    void signupAndLogin_Integration_Success() throws Exception {
        // given - 회원가입
        var signupRequest = TestAuthRequestFactory.createUserRequest("test@example.com", "password123", "테스트유저");

        mockMvc.perform(post("/api/v1/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
            .andExpect(status().isCreated());

        // when - 로그인
        var loginRequest = TestAuthRequestFactory.loginRequest("test@example.com", "password123");

        // then
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.token").exists());
    }

    @Test
    @DisplayName("중복 이메일로 회원가입 시 409 에러를 반환한다")
    void signup_DuplicateEmail_Returns409() throws Exception {
        // given - 첫 번째 회원가입
        var request1 = TestAuthRequestFactory.createUserRequest("test@example.com", "password123", "테스트유저1");
        mockMvc.perform(post("/api/v1/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request1)))
            .andExpect(status().isCreated());

        // when - 중복 이메일로 회원가입
        var request2 = TestAuthRequestFactory.createUserRequest("test@example.com", "password456", "테스트유저2");

        // then
        mockMvc.perform(post("/api/v1/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request2)))
            .andExpect(status().isConflict());
    }

    @Test
    @DisplayName("잘못된 비밀번호로 로그인 시 401 에러를 반환한다")
    void login_InvalidPassword_Returns401() throws Exception {
        // given - 회원가입
        var signupRequest = TestAuthRequestFactory.createUserRequest("test@example.com", "password123", "테스트유저");
        mockMvc.perform(post("/api/v1/auth/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(signupRequest)))
            .andExpect(status().isCreated());

        // when - 잘못된 비밀번호로 로그인
        var loginRequest = TestAuthRequestFactory.loginRequest("test@example.com", "wrongPassword");

        // then
        mockMvc.perform(post("/api/v1/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
            .andExpect(status().isUnauthorized());
    }
}


package kbo.today.common.exception;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
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

@WebMvcTest(controllers = TestExceptionController.class)
@Import({GlobalExceptionHandler.class, GlobalExceptionHandlerTest.TestSecurityConfig.class})
@DisplayName("GlobalExceptionHandler 테스트")
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

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
    @DisplayName("BusinessException을 올바르게 처리한다")
    void handleBusinessException_Success() throws Exception {
        // when & then
        mockMvc.perform(post("/test/business-exception")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.code").value("USER_001")) // ErrorCode의 실제 code 값
            .andExpect(jsonPath("$.message").value("이미 존재하는 이메일입니다."));
    }

    @Test
    @DisplayName("MethodArgumentNotValidException을 올바르게 처리한다")
    void handleValidationException_Success() throws Exception {
        // given
        TestExceptionController.TestRequest invalidRequest = new TestExceptionController.TestRequest("", ""); // 빈 값으로 validation 실패

        // when & then
        mockMvc.perform(post("/test/validation")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("VALID_001")) // ErrorCode의 실제 code 값
            .andExpect(jsonPath("$.fieldErrors").exists());
    }

    @Test
    @DisplayName("IllegalArgumentException을 올바르게 처리한다")
    void handleIllegalArgumentException_Success() throws Exception {
        // when & then
        mockMvc.perform(post("/test/illegal-argument")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.code").value("GEN_001")) // ErrorCode의 실제 code 값
            .andExpect(jsonPath("$.message").value("잘못된 인자입니다."));
    }

    @Test
    @DisplayName("일반 Exception을 올바르게 처리한다")
    void handleGenericException_Success() throws Exception {
        // when & then
        mockMvc.perform(post("/test/generic-exception")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.code").value("GEN_500")); // ErrorCode의 실제 code 값
    }
}


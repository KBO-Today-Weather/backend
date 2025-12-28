package kbo.today.adapter.in.web.stadium;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kbo.today.domain.stadium.Stadium;
import kbo.today.domain.stadium.usecase.GetStadiumUseCase;
import kbo.today.domain.team.Team;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.test.web.servlet.MockMvc;
import org.mockito.Mockito;

@WebMvcTest(controllers = StadiumController.class)
@Import(StadiumControllerTest.TestSecurityConfig.class)
@DisplayName("StadiumController 단위 테스트")
class StadiumControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GetStadiumUseCase getStadiumUseCase;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public GetStadiumUseCase getStadiumUseCase() {
            return Mockito.mock(GetStadiumUseCase.class);
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
    @DisplayName("모든 구장 목록을 조회한다")
    void getAllStadiums_Success() throws Exception {
        // given
        Team team = Team.create("한화 이글스", "대전", "logo.png");
        Stadium stadium1 = Stadium.create(team, "대전 한화생명 이글스파크", "대전광역시 중구", 20000);
        Stadium stadium2 = Stadium.create(team, "잠실야구장", "서울특별시 송파구", 25000);

        List<Stadium> stadiums = Arrays.asList(stadium1, stadium2);
        given(getStadiumUseCase.getAll()).willReturn(stadiums);

        // when & then
        mockMvc.perform(get("/api/v1/stadiums"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$[0].name").value("대전 한화생명 이글스파크"))
            .andExpect(jsonPath("$[1].name").value("잠실야구장"));
    }

    @Test
    @DisplayName("구장 ID로 구장 상세 정보를 조회한다")
    void getStadiumById_Success() throws Exception {
        // given
        Team team = Team.create("한화 이글스", "대전", "logo.png");
        Stadium stadium = Stadium.create(team, "대전 한화생명 이글스파크", "대전광역시 중구", 20000);

        given(getStadiumUseCase.getById(1L)).willReturn(Optional.of(stadium));

        // when & then
        mockMvc.perform(get("/api/v1/stadiums/1"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.name").value("대전 한화생명 이글스파크"))
            .andExpect(jsonPath("$.address").value("대전광역시 중구"))
            .andExpect(jsonPath("$.capacity").value(20000));
    }

    @Test
    @DisplayName("존재하지 않는 구장 ID로 조회 시 404를 반환한다")
    void getStadiumById_NotFound_Returns404() throws Exception {
        // given
        given(getStadiumUseCase.getById(999L)).willReturn(Optional.empty());

        // when & then
        mockMvc.perform(get("/api/v1/stadiums/999"))
            .andExpect(status().isNotFound());
    }
}


package kbo.today.adapter.in.web.team;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;
import kbo.today.common.exception.GlobalExceptionHandler;
import kbo.today.domain.team.Team;
import kbo.today.domain.team.TeamStatus;
import kbo.today.domain.team.usecase.GetTeamsUseCase;
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

@WebMvcTest(controllers = TeamController.class)
@Import({
    TeamControllerTest.TestConfig.class,
    TeamControllerTest.TestSecurityConfig.class,
    GlobalExceptionHandler.class
})
@DisplayName("TeamController 단위 테스트")
class TeamControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private GetTeamsUseCase getTeamsUseCase;

    @TestConfiguration
    static class TestConfig {
        @Bean
        public GetTeamsUseCase getTeamsUseCase() {
            return Mockito.mock(GetTeamsUseCase.class);
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
    @DisplayName("팀 목록 조회 API가 정상적으로 동작한다")
    void getBySeason_returnsOk() throws Exception {
        List<Team> teams = List.of(
            Team.fromPersistenceWithStandings(1L, "KIA 타이거즈", "광주", null, TeamStatus.ACTIVE, 1, 87, 55, 2, 0.613, null, null, null, null),
            Team.fromPersistenceWithStandings(2L, "삼성 라이온즈", "대구", null, TeamStatus.ACTIVE, 2, 83, 59, 2, 0.585, 4, null, null, null)
        );
        when(getTeamsUseCase.getBySeason(null)).thenReturn(teams);

        mockMvc.perform(get("/api/v1/teams"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].rank").value(1))
            .andExpect(jsonPath("$[0].teamName").value("KIA 타이거즈"))
            .andExpect(jsonPath("$[0].wins").value(87))
            .andExpect(jsonPath("$[0].losses").value(55))
            .andExpect(jsonPath("$[0].draws").value(2))
            .andExpect(jsonPath("$[0].winningPercentage").value(0.613))
            .andExpect(jsonPath("$[0].gamesBack").isEmpty())
            .andExpect(jsonPath("$[1].rank").value(2))
            .andExpect(jsonPath("$[1].teamName").value("삼성 라이온즈"))
            .andExpect(jsonPath("$[1].gamesBack").value(4));
    }

    @Test
    @DisplayName("시즌 파라미터로 팀 목록을 조회할 수 있다")
    void getBySeason_withSeason_returnsOk() throws Exception {
        when(getTeamsUseCase.getBySeason(2024)).thenReturn(List.of());

        mockMvc.perform(get("/api/v1/teams").param("season", "2024"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(0));
    }
}

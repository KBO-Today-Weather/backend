package kbo.today.adapter.in.web.team;

import java.util.List;
import kbo.today.adapter.in.web.team.dto.TeamResponse;
import kbo.today.domain.team.Team;
import kbo.today.domain.team.usecase.GetTeamsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Team", description = "팀 관련 API")
@RestController
@RequestMapping("/api/v1/teams")
public class TeamController {

    private final GetTeamsUseCase getTeamsUseCase;

    public TeamController(GetTeamsUseCase getTeamsUseCase) {
        this.getTeamsUseCase = getTeamsUseCase;
    }

    @Operation(summary = "팀 목록 조회 (시즌별 순위)", description = "시즌별 팀 순위를 조회합니다. (순위, 팀명, 승, 패, 무, 승률, 게임차)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공")
    })
    @GetMapping
    public ResponseEntity<List<TeamResponse>> getBySeason(
        @Parameter(description = "시즌 연도 (미입력 시 현재 연도)", example = "2024")
        @RequestParam(required = false) Integer season
    ) {
        List<Team> teams = getTeamsUseCase.getBySeason(season);
        List<TeamResponse> responses = teams.stream()
            .map(TeamResponse::from)
            .toList();
        return ResponseEntity.ok(responses);
    }
}

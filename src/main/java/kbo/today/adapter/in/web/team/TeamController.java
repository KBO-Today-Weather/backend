package kbo.today.adapter.in.web.team;

import java.util.List;
import kbo.today.adapter.in.web.team.dto.TeamComparisonResponse;
import kbo.today.adapter.in.web.team.dto.TeamHistoryResponse;
import kbo.today.adapter.in.web.team.dto.TeamResponse;
import kbo.today.domain.team.Team;
import kbo.today.domain.team.usecase.GetTeamComparisonUseCase;
import kbo.today.domain.team.usecase.GetTeamHistoryUseCase;
import kbo.today.domain.team.usecase.GetTeamsUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Team", description = "팀 관련 API")
@RestController
@RequestMapping("/api/v1/teams")
@RequiredArgsConstructor
public class TeamController {

    private final GetTeamsUseCase getTeamsUseCase;
    private final GetTeamComparisonUseCase getTeamComparisonUseCase;
    private final GetTeamHistoryUseCase getTeamHistoryUseCase;

    @Operation(summary = "팀 목록 조회 (시즌별 순위)")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "조회 성공") })
    @GetMapping
    public ResponseEntity<List<TeamResponse>> getBySeason(
        @Parameter(description = "시즌 연도 (미입력 시 현재 연도)", example = "2024")
        @RequestParam(required = false) Integer season
    ) {
        List<Team> teams = getTeamsUseCase.getBySeason(season);
        return ResponseEntity.ok(teams.stream().map(TeamResponse::from).toList());
    }

    @Operation(summary = "팀 비교 분석", description = "두 팀의 종합 능력치 비교 (타율, 홈런, 방어율, 도루, 수비율)")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공"),
        @ApiResponse(responseCode = "404", description = "팀 또는 시즌 데이터 없음")
    })
    @GetMapping("/compare")
    public ResponseEntity<TeamComparisonResponse> getComparison(
        @Parameter(description = "팀1 ID", required = true) @RequestParam Long team1Id,
        @Parameter(description = "팀2 ID", required = true) @RequestParam Long team2Id,
        @Parameter(description = "시즌 연도 (미입력 시 현재 연도)", example = "2024") @RequestParam(required = false) Integer season
    ) {
        return getTeamComparisonUseCase.getComparison(team1Id, team2Id, season)
            .map(TeamComparisonResponse::from)
            .map(ResponseEntity::ok)
            .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "과거 기록", description = "팀별 시즌별 순위, 승패, 승률, 포스트시즌 결과")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "조회 성공") })
    @GetMapping("/{teamId}/history")
    public ResponseEntity<List<TeamHistoryResponse>> getHistory(
        @Parameter(description = "팀 ID", required = true) @PathVariable Long teamId,
        @Parameter(description = "시즌 시작 연도", example = "2020") @RequestParam(required = false) Integer seasonFrom,
        @Parameter(description = "시즌 종료 연도", example = "2024") @RequestParam(required = false) Integer seasonTo
    ) {
        List<TeamHistoryResponse> responses = getTeamHistoryUseCase.getHistory(teamId, seasonFrom, seasonTo)
            .stream()
            .map(TeamHistoryResponse::from)
            .toList();
        return ResponseEntity.ok(responses);
    }
}

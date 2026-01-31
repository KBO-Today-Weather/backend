package kbo.today.adapter.in.web.team.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import kbo.today.domain.team.Team;

@Schema(description = "팀 응답 (순위 포함)")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record TeamResponse(
    @Schema(description = "순위", example = "1")
    int rank,
    @Schema(description = "팀명", example = "KIA 타이거즈")
    String teamName,
    @Schema(description = "승", example = "87")
    int wins,
    @Schema(description = "패", example = "55")
    int losses,
    @Schema(description = "무", example = "2")
    int draws,
    @Schema(description = "승률", example = "0.613")
    double winningPercentage,
    @Schema(description = "게임차 (1위는 null)", example = "4")
    Integer gamesBack
) {
    public static TeamResponse from(Team team) {
        return new TeamResponse(
            team.getRank() != null ? team.getRank() : 0,
            team.getName(),
            team.getWins() != null ? team.getWins() : 0,
            team.getLosses() != null ? team.getLosses() : 0,
            team.getDraws() != null ? team.getDraws() : 0,
            team.getWinningPercentage() != null ? team.getWinningPercentage() : 0.0,
            team.getGamesBack()
        );
    }
}

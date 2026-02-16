package kbo.today.adapter.in.web.team.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kbo.today.domain.team.TeamHistoryRecord;

@Schema(description = "팀 과거 기록 응답")
public record TeamHistoryResponse(
    @Schema(description = "시즌", example = "2024") int season,
    @Schema(description = "순위", example = "3") int rank,
    @Schema(description = "승", example = "80") int wins,
    @Schema(description = "패", example = "56") int losses,
    @Schema(description = "승률", example = "0.588") double winRate,
    @Schema(description = "포스트시즌", example = "준우승") String postseasonResult
) {
    public static TeamHistoryResponse from(TeamHistoryRecord record) {
        return new TeamHistoryResponse(
            record.season(),
            record.rank(),
            record.wins(),
            record.losses(),
            record.winRate(),
            record.postseasonResult()
        );
    }
}

package kbo.today.adapter.in.web.team.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import kbo.today.domain.team.TeamComparison;
import kbo.today.domain.team.TeamSeasonStats;

@Schema(description = "팀 비교 분석 응답")
public record TeamComparisonResponse(
    @Schema(description = "팀1 ID") Long team1Id,
    @Schema(description = "팀1명") String team1Name,
    @Schema(description = "팀2 ID") Long team2Id,
    @Schema(description = "팀2명") String team2Name,
    @Schema(description = "팀1 주요 스탯") TeamSeasonStatsResponse team1Stats,
    @Schema(description = "팀2 주요 스탯") TeamSeasonStatsResponse team2Stats
) {
    public static TeamComparisonResponse from(TeamComparison comparison) {
        return new TeamComparisonResponse(
            comparison.team1Id(),
            comparison.team1Name(),
            comparison.team2Id(),
            comparison.team2Name(),
            TeamSeasonStatsResponse.from(comparison.team1Stats()),
            TeamSeasonStatsResponse.from(comparison.team2Stats())
        );
    }

    @Schema(description = "팀 시즌 주요 스탯 (타율, 홈런, 방어율, 도루, 수비율)")
    public record TeamSeasonStatsResponse(
        @Schema(description = "타율", example = "0.275") double battingAverage,
        @Schema(description = "홈런", example = "156") int homeRuns,
        @Schema(description = "방어율", example = "3.85") double era,
        @Schema(description = "도루", example = "98") int stolenBases,
        @Schema(description = "수비율", example = "0.982") double fieldingPercentage
    ) {
        static TeamSeasonStatsResponse from(TeamSeasonStats stats) {
            return new TeamSeasonStatsResponse(
                stats.battingAverage(),
                stats.homeRuns(),
                stats.era(),
                stats.stolenBases(),
                stats.fieldingPercentage()
            );
        }
    }
}

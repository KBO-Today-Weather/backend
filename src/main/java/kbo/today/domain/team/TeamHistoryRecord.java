package kbo.today.domain.team;

public record TeamHistoryRecord(
    int season,
    int rank,
    int wins,
    int losses,
    double winRate,
    String postseasonResult
) {}

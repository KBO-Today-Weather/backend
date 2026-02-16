package kbo.today.domain.team;

public record TeamSeasonStats(
    double battingAverage,
    int homeRuns,
    double era,
    int stolenBases,
    double fieldingPercentage
) {}

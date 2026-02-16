package kbo.today.domain.team;

public record TeamComparison(
    Long team1Id,
    String team1Name,
    Long team2Id,
    String team2Name,
    TeamSeasonStats team1Stats,
    TeamSeasonStats team2Stats
) {}

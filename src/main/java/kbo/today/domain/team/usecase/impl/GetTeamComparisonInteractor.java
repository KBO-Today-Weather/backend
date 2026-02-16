package kbo.today.domain.team.usecase.impl;

import java.time.Year;
import java.util.Optional;
import kbo.today.domain.team.TeamComparison;
import kbo.today.domain.team.port.TeamComparisonQueryPort;
import kbo.today.domain.team.usecase.GetTeamComparisonUseCase;

public class GetTeamComparisonInteractor implements GetTeamComparisonUseCase {

    private final TeamComparisonQueryPort teamComparisonQueryPort;

    public GetTeamComparisonInteractor(TeamComparisonQueryPort teamComparisonQueryPort) {
        this.teamComparisonQueryPort = teamComparisonQueryPort;
    }

    @Override
    public Optional<TeamComparison> getComparison(Long team1Id, Long team2Id, Integer season) {
        int targetSeason = season != null ? season : Year.now().getValue();
        return teamComparisonQueryPort.findComparison(team1Id, team2Id, targetSeason);
    }
}

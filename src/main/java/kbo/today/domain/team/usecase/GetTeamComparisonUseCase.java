package kbo.today.domain.team.usecase;

import java.util.Optional;
import kbo.today.domain.team.TeamComparison;

public interface GetTeamComparisonUseCase {

    Optional<TeamComparison> getComparison(Long team1Id, Long team2Id, Integer season);
}

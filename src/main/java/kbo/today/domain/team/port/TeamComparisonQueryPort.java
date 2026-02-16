package kbo.today.domain.team.port;

import java.util.Optional;
import kbo.today.domain.team.TeamComparison;

public interface TeamComparisonQueryPort {

    Optional<TeamComparison> findComparison(Long team1Id, Long team2Id, int season);
}

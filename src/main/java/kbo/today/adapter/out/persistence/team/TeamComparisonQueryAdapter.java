package kbo.today.adapter.out.persistence.team;

import java.util.Optional;
import kbo.today.domain.team.TeamComparison;
import kbo.today.domain.team.TeamSeasonStats;
import kbo.today.domain.team.port.TeamComparisonQueryPort;
import org.springframework.stereotype.Component;

@Component
public class TeamComparisonQueryAdapter implements TeamComparisonQueryPort {

    private final TeamSeasonQueryRepository teamSeasonQueryRepository;

    public TeamComparisonQueryAdapter(TeamSeasonQueryRepository teamSeasonQueryRepository) {
        this.teamSeasonQueryRepository = teamSeasonQueryRepository;
    }

    @Override
    public Optional<TeamComparison> findComparison(Long team1Id, Long team2Id, int season) {
        Optional<TeamSeasonJpaEntity> opt1 = teamSeasonQueryRepository.findByTeamIdAndSeason(team1Id, season);
        Optional<TeamSeasonJpaEntity> opt2 = teamSeasonQueryRepository.findByTeamIdAndSeason(team2Id, season);
        if (opt1.isEmpty() || opt2.isEmpty()) {
            return Optional.empty();
        }
        TeamSeasonJpaEntity e1 = opt1.get();
        TeamSeasonJpaEntity e2 = opt2.get();
        TeamSeasonStats stats1 = toStats(e1);
        TeamSeasonStats stats2 = toStats(e2);
        return Optional.of(new TeamComparison(
            e1.getTeam().getId(),
            e1.getTeam().getName(),
            e2.getTeam().getId(),
            e2.getTeam().getName(),
            stats1,
            stats2
        ));
    }

    private static TeamSeasonStats toStats(TeamSeasonJpaEntity e) {
        return new TeamSeasonStats(
            e.getBattingAverage() != null ? e.getBattingAverage() : 0.0,
            e.getHomeRuns() != null ? e.getHomeRuns() : 0,
            e.getEra() != null ? e.getEra() : 0.0,
            e.getStolenBases() != null ? e.getStolenBases() : 0,
            e.getFieldingPercentage() != null ? e.getFieldingPercentage() : 0.0
        );
    }
}

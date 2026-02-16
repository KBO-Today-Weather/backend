package kbo.today.adapter.out.persistence.team;

import java.util.List;
import kbo.today.domain.team.TeamHistoryRecord;
import kbo.today.domain.team.port.TeamHistoryQueryPort;
import org.springframework.stereotype.Component;

@Component
public class TeamHistoryQueryAdapter implements TeamHistoryQueryPort {

    private final TeamSeasonQueryRepository teamSeasonQueryRepository;

    public TeamHistoryQueryAdapter(TeamSeasonQueryRepository teamSeasonQueryRepository) {
        this.teamSeasonQueryRepository = teamSeasonQueryRepository;
    }

    @Override
    public List<TeamHistoryRecord> findHistoryByTeamId(Long teamId, Integer seasonFrom, Integer seasonTo) {
        return teamSeasonQueryRepository.findByTeamIdOrderBySeasonDesc(teamId, seasonFrom, seasonTo)
            .stream()
            .map(this::toRecord)
            .toList();
    }

    private TeamHistoryRecord toRecord(TeamSeasonJpaEntity e) {
        return new TeamHistoryRecord(
            e.getSeason(),
            e.getRank(),
            e.getWins(),
            e.getLosses(),
            e.getWinningPercentage(),
            e.getPostseasonResult() != null ? e.getPostseasonResult() : ""
        );
    }
}

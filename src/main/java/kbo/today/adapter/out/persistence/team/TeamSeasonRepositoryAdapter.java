package kbo.today.adapter.out.persistence.team;

import java.util.List;
import kbo.today.domain.team.Team;
import kbo.today.domain.team.port.TeamQueryPort;
import org.springframework.stereotype.Component;

@Component
public class TeamSeasonRepositoryAdapter implements TeamQueryPort {

    private final TeamSeasonQueryRepository teamSeasonQueryRepository;

    public TeamSeasonRepositoryAdapter(TeamSeasonQueryRepository teamSeasonQueryRepository) {
        this.teamSeasonQueryRepository = teamSeasonQueryRepository;
    }

    @Override
    public List<Team> findTeamsBySeason(int season) {
        return teamSeasonQueryRepository.findBySeasonOrderByRankAsc(season)
            .stream()
            .map(TeamSeasonJpaEntity::toDomain)
            .toList();
    }
}

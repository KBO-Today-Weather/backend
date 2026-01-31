package kbo.today.domain.team.usecase.impl;

import java.time.Year;
import java.util.List;
import kbo.today.domain.team.Team;
import kbo.today.domain.team.port.TeamQueryPort;
import kbo.today.domain.team.usecase.GetTeamsUseCase;

public class GetTeamsInteractor implements GetTeamsUseCase {

    private final TeamQueryPort teamQueryPort;

    public GetTeamsInteractor(TeamQueryPort teamQueryPort) {
        this.teamQueryPort = teamQueryPort;
    }

    @Override
    public List<Team> getBySeason(Integer season) {
        int targetSeason = season != null ? season : Year.now().getValue();
        return teamQueryPort.findTeamsBySeason(targetSeason);
    }
}

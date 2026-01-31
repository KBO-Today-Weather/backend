package kbo.today.domain.team.usecase;

import java.util.List;
import kbo.today.domain.team.Team;

public interface GetTeamsUseCase {

    List<Team> getBySeason(Integer season);
}

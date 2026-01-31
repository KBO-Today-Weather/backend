package kbo.today.domain.team.port;

import java.util.List;
import kbo.today.domain.team.Team;

public interface TeamQueryPort {

    List<Team> findTeamsBySeason(int season);
}

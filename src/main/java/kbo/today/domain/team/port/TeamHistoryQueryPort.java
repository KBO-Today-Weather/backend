package kbo.today.domain.team.port;

import java.util.List;
import kbo.today.domain.team.TeamHistoryRecord;

public interface TeamHistoryQueryPort {

    List<TeamHistoryRecord> findHistoryByTeamId(Long teamId, Integer seasonFrom, Integer seasonTo);
}

package kbo.today.domain.team.usecase;

import java.util.List;
import kbo.today.domain.team.TeamHistoryRecord;

public interface GetTeamHistoryUseCase {

    List<TeamHistoryRecord> getHistory(Long teamId, Integer seasonFrom, Integer seasonTo);
}

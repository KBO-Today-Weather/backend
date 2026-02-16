package kbo.today.domain.team.usecase.impl;

import java.util.List;
import kbo.today.domain.team.TeamHistoryRecord;
import kbo.today.domain.team.port.TeamHistoryQueryPort;
import kbo.today.domain.team.usecase.GetTeamHistoryUseCase;

public class GetTeamHistoryInteractor implements GetTeamHistoryUseCase {

    private final TeamHistoryQueryPort teamHistoryQueryPort;

    public GetTeamHistoryInteractor(TeamHistoryQueryPort teamHistoryQueryPort) {
        this.teamHistoryQueryPort = teamHistoryQueryPort;
    }

    @Override
    public List<TeamHistoryRecord> getHistory(Long teamId, Integer seasonFrom, Integer seasonTo) {
        return teamHistoryQueryPort.findHistoryByTeamId(teamId, seasonFrom, seasonTo);
    }
}

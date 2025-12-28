package kbo.today.adapter.in.web.stadium.dto;

import java.util.List;
import kbo.today.domain.stadium.Stadium;
import kbo.today.domain.team.Team;

public record StadiumResponse(
    Long id,
    TeamInfo team,
    String name,
    String address,
    Integer capacity,
    List<StadiumFoodInfo> foods,
    List<StadiumSeatInfo> seats,
    List<StadiumTransportInfo> transports
) {
    public static StadiumResponse from(Stadium stadium) {
        Team team = stadium.getTeam();
        TeamInfo teamInfo = team != null 
            ? new TeamInfo(team.getId(), team.getName(), team.getCity(), team.getLogoUrl())
            : null;

        List<StadiumFoodInfo> foods = stadium.getFoods().stream()
            .map(food -> new StadiumFoodInfo(
                food.getId(),
                food.getName(),
                food.getPrice(),
                food.getLocation(),
                food.getRating()
            ))
            .toList();

        List<StadiumSeatInfo> seats = stadium.getSeats().stream()
            .map(seat -> new StadiumSeatInfo(
                seat.getId(),
                seat.getSectionName(),
                seat.getSeatType(),
                seat.getDescription(),
                seat.getRecommendation()
            ))
            .toList();

        List<StadiumTransportInfo> transports = stadium.getTransports().stream()
            .map(transport -> new StadiumTransportInfo(
                transport.getId(),
                transport.getTransportType(),
                transport.getRoute(),
                transport.getDescription(),
                transport.getTip()
            ))
            .toList();

        return new StadiumResponse(
            stadium.getId(),
            teamInfo,
            stadium.getName(),
            stadium.getAddress(),
            stadium.getCapacity(),
            foods,
            seats,
            transports
        );
    }
}


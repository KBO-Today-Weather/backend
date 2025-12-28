package kbo.today.adapter.in.web.stadium.dto;

import kbo.today.domain.stadium.SeatType;

public record StadiumSeatInfo(
    Long id,
    String sectionName,
    SeatType seatType,
    String description,
    String recommendation
) {
}


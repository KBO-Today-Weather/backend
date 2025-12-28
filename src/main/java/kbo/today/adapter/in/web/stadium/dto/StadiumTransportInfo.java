package kbo.today.adapter.in.web.stadium.dto;

import kbo.today.domain.stadium.TransportType;

public record StadiumTransportInfo(
    Long id,
    TransportType transportType,
    String route,
    String description,
    String tip
) {
}


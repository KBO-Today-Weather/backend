package kbo.today.adapter.in.web.favorite.dto;

import kbo.today.domain.favorite.FavoriteStadium;

public record FavoriteStadiumResponse(
    Long id,
    Long userId,
    Long stadiumId
) {

    public static FavoriteStadiumResponse from(FavoriteStadium favoriteStadium) {
        return new FavoriteStadiumResponse(
            favoriteStadium.getId(),
            favoriteStadium.getUserId(),
            favoriteStadium.getStadiumId()
        );
    }
}


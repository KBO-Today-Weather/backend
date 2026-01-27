package kbo.today.domain.favorite.usecase;

import kbo.today.domain.favorite.FavoriteStadium;

public interface AddFavoriteStadiumUseCase {

    FavoriteStadium add(AddFavoriteStadiumCommand command);
}


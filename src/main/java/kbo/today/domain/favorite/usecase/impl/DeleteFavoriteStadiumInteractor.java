package kbo.today.domain.favorite.usecase.impl;

import kbo.today.domain.favorite.FavoriteStadium;
import kbo.today.domain.favorite.port.FavoriteStadiumRepositoryPort;
import kbo.today.domain.favorite.usecase.DeleteFavoriteStadiumCommand;
import kbo.today.domain.favorite.usecase.DeleteFavoriteStadiumUseCase;

public class DeleteFavoriteStadiumInteractor implements DeleteFavoriteStadiumUseCase {

    private final FavoriteStadiumRepositoryPort favoriteRepository;

    public DeleteFavoriteStadiumInteractor(FavoriteStadiumRepositoryPort favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    @Override
    public void delete(DeleteFavoriteStadiumCommand command) {
        Long userId = command.getUserId();
        Long stadiumId = command.getStadiumId();

        FavoriteStadium favorite = favoriteRepository.findByUserIdAndStadiumId(userId, stadiumId)
            .orElse(null);

        if (favorite != null && favorite.getId() != null) {
            favoriteRepository.delete(favorite);
        }
    }
}


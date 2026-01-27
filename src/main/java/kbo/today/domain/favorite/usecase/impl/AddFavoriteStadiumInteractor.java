package kbo.today.domain.favorite.usecase.impl;

import java.util.Optional;
import kbo.today.common.exception.StadiumNotFoundException;
import kbo.today.common.exception.UserNotFoundException;
import kbo.today.domain.favorite.FavoriteStadium;
import kbo.today.domain.favorite.port.FavoriteStadiumRepositoryPort;
import kbo.today.domain.favorite.usecase.AddFavoriteStadiumCommand;
import kbo.today.domain.favorite.usecase.AddFavoriteStadiumUseCase;
import kbo.today.domain.stadium.Stadium;
import kbo.today.domain.stadium.port.StadiumRepositoryPort;
import kbo.today.domain.user.domain.User;
import kbo.today.domain.user.port.UserRepositoryPort;

public class AddFavoriteStadiumInteractor implements AddFavoriteStadiumUseCase {

    private final FavoriteStadiumRepositoryPort favoriteRepository;
    private final UserRepositoryPort userRepository;
    private final StadiumRepositoryPort stadiumRepository;

    public AddFavoriteStadiumInteractor(
        FavoriteStadiumRepositoryPort favoriteRepository,
        UserRepositoryPort userRepository,
        StadiumRepositoryPort stadiumRepository
    ) {
        this.favoriteRepository = favoriteRepository;
        this.userRepository = userRepository;
        this.stadiumRepository = stadiumRepository;
    }

    @Override
    public FavoriteStadium add(AddFavoriteStadiumCommand command) {
        Long userId = command.getUserId();
        Long stadiumId = command.getStadiumId();

        Optional<User> user = userRepository.findById(userId);
        if (user.isEmpty()) {
            throw new UserNotFoundException();
        }

        Optional<Stadium> stadium = stadiumRepository.findById(stadiumId);
        if (stadium.isEmpty()) {
            throw new StadiumNotFoundException();
        }

        return favoriteRepository.findByUserIdAndStadiumId(userId, stadiumId)
            .orElseGet(() -> favoriteRepository.save(FavoriteStadium.create(userId, stadiumId)));
    }
}


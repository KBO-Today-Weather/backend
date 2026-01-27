package kbo.today.domain.favorite.port;

import java.util.List;
import java.util.Optional;
import kbo.today.domain.favorite.FavoriteStadium;

public interface FavoriteStadiumRepositoryPort {

    FavoriteStadium save(FavoriteStadium favoriteStadium);

    Optional<FavoriteStadium> findByUserIdAndStadiumId(Long userId, Long stadiumId);

    List<FavoriteStadium> findByUserId(Long userId);

    void delete(FavoriteStadium favoriteStadium);
}


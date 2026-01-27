package kbo.today.adapter.out.persistence.favorite;

import java.util.List;
import java.util.Optional;
import kbo.today.adapter.out.persistence.stadium.StadiumJpaEntity;
import kbo.today.adapter.out.persistence.stadium.StadiumJpaRepository;
import kbo.today.adapter.out.persistence.user.UserJpaEntity;
import kbo.today.adapter.out.persistence.user.UserJpaRepository;
import kbo.today.common.exception.StadiumNotFoundException;
import kbo.today.common.exception.UserNotFoundException;
import kbo.today.domain.favorite.FavoriteStadium;
import kbo.today.domain.favorite.port.FavoriteStadiumRepositoryPort;
import org.springframework.stereotype.Component;

@Component
public class FavoriteStadiumRepositoryAdapter implements FavoriteStadiumRepositoryPort {

    private final FavoriteStadiumJpaRepository favoriteJpaRepository;
    private final UserJpaRepository userJpaRepository;
    private final StadiumJpaRepository stadiumJpaRepository;

    public FavoriteStadiumRepositoryAdapter(
        FavoriteStadiumJpaRepository favoriteJpaRepository,
        UserJpaRepository userJpaRepository,
        StadiumJpaRepository stadiumJpaRepository
    ) {
        this.favoriteJpaRepository = favoriteJpaRepository;
        this.userJpaRepository = userJpaRepository;
        this.stadiumJpaRepository = stadiumJpaRepository;
    }

    @Override
    public FavoriteStadium save(FavoriteStadium favoriteStadium) {
        Long userId = favoriteStadium.getUserId();
        Long stadiumId = favoriteStadium.getStadiumId();

        if (userId == null || stadiumId == null) {
            throw new IllegalArgumentException("UserId and StadiumId must not be null when saving favorite.");
        }

        UserJpaEntity user = userJpaRepository.findById(userId)
            .orElseThrow(UserNotFoundException::new);
        StadiumJpaEntity stadium = stadiumJpaRepository.findById(stadiumId)
            .orElseThrow(StadiumNotFoundException::new);

        FavoriteStadiumJpaEntity entity = FavoriteStadiumJpaEntity.from(favoriteStadium, user, stadium);
        FavoriteStadiumJpaEntity savedEntity = favoriteJpaRepository.save(entity);
        return savedEntity.toDomain();
    }

    @Override
    public Optional<FavoriteStadium> findByUserIdAndStadiumId(Long userId, Long stadiumId) {
        return favoriteJpaRepository.findByUser_IdAndStadium_Id(userId, stadiumId)
            .map(FavoriteStadiumJpaEntity::toDomain);
    }

    @Override
    public List<FavoriteStadium> findByUserId(Long userId) {
        return favoriteJpaRepository.findByUser_Id(userId).stream()
            .map(FavoriteStadiumJpaEntity::toDomain)
            .toList();
    }

    @Override
    public void delete(FavoriteStadium favoriteStadium) {
        Long id = favoriteStadium.getId();
        if (id != null) {
            favoriteJpaRepository.deleteById(id);
        }
    }
}


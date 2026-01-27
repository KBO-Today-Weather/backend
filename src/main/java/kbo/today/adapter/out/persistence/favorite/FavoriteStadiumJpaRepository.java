package kbo.today.adapter.out.persistence.favorite;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteStadiumJpaRepository extends JpaRepository<FavoriteStadiumJpaEntity, Long> {

    Optional<FavoriteStadiumJpaEntity> findByUser_IdAndStadium_Id(Long userId, Long stadiumId);

    List<FavoriteStadiumJpaEntity> findByUser_Id(Long userId);
}


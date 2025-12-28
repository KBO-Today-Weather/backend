package kbo.today.adapter.out.persistence.game;

import org.springframework.data.jpa.repository.JpaRepository;

public interface GameJpaRepository extends JpaRepository<GameJpaEntity, Long> {
}


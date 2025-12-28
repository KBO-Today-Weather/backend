package kbo.today.adapter.out.persistence.stadium;

import kbo.today.domain.stadium.Stadium;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StadiumJpaRepository extends JpaRepository<Stadium, Long> {
}


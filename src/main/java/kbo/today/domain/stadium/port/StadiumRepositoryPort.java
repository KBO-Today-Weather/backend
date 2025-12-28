package kbo.today.domain.stadium.port;

import java.util.List;
import java.util.Optional;
import kbo.today.domain.stadium.Stadium;

public interface StadiumRepositoryPort {
    Optional<Stadium> findById(Long id);
    List<Stadium> findAll();
    Optional<Stadium> findByIdForWeather(Long id);
}


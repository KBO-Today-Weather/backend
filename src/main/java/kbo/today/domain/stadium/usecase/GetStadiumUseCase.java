package kbo.today.domain.stadium.usecase;

import java.util.List;
import java.util.Optional;
import kbo.today.domain.stadium.Stadium;

public interface GetStadiumUseCase {
    Optional<Stadium> getById(Long id);
    List<Stadium> getAll();
}


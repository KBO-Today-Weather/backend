package kbo.today.adapter.out.persistence.stadium;

import java.util.List;
import java.util.Optional;
import kbo.today.domain.stadium.Stadium;
import kbo.today.domain.stadium.port.StadiumRepositoryPort;
import org.springframework.stereotype.Component;

@Component
public class StadiumRepositoryAdapter implements StadiumRepositoryPort {

    private final StadiumQueryRepository queryRepository;

    public StadiumRepositoryAdapter(StadiumQueryRepository queryRepository) {
        this.queryRepository = queryRepository;
    }

    @Override
    public Optional<Stadium> findById(Long id) {
        return queryRepository.findByIdWithDetails(id);
    }

    @Override
    public List<Stadium> findAll() {
        return queryRepository.findAllWithTeam();
    }

    @Override
    public Optional<Stadium> findByIdForWeather(Long id) {
        return queryRepository.findByIdForWeather(id);
    }
}


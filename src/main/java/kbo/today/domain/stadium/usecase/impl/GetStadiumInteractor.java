package kbo.today.domain.stadium.usecase.impl;

import java.util.List;
import java.util.Optional;
import kbo.today.domain.stadium.Stadium;
import kbo.today.domain.stadium.port.StadiumRepositoryPort;
import kbo.today.domain.stadium.usecase.GetStadiumUseCase;

public class GetStadiumInteractor implements GetStadiumUseCase {

    private final StadiumRepositoryPort stadiumRepository;

    public GetStadiumInteractor(StadiumRepositoryPort stadiumRepository) {
        this.stadiumRepository = stadiumRepository;
    }

    @Override
    public Optional<Stadium> getById(Long id) {
        return stadiumRepository.findById(id);
    }

    @Override
    public List<Stadium> getAll() {
        return stadiumRepository.findAll();
    }
}


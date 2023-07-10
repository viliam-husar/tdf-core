package cz.krvotok.tdf.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import cz.krvotok.tdf.domain.model.aggregate.Tour;

public interface TourRepository {

    UUID persist(Tour tour);

    List<Tour> findAll();

    Optional<Tour> findById(UUID id);

    void deleteById(UUID id);
}

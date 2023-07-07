package cz.krvotok.tdf.domain.repository;

import java.util.Optional;

import cz.krvotok.tdf.domain.model.aggregate.TourDeFelvidek;

public interface TourDeFelvidekRepository {
    Optional<TourDeFelvidek> findByYear(int year);
}
package cz.krvotok.tdf.domain.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import cz.krvotok.tdf.domain.model.aggregate.Search;

public interface SearchRepository {

    UUID persist(Search search);

    List<Search> findAll();

    Optional<Search> findById(UUID id);

    void deleteById(UUID id);
}

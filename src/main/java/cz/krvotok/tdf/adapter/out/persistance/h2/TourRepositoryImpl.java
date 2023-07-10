package cz.krvotok.tdf.adapter.out.persistance.h2;

import io.micronaut.transaction.annotation.ReadOnly;
import jakarta.inject.Singleton;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import cz.krvotok.tdf.domain.model.aggregate.Tour;
import cz.krvotok.tdf.domain.repository.TourRepository;
import io.micronaut.transaction.annotation.TransactionalAdvice;

@Singleton
public class TourRepositoryImpl implements TourRepository {

    private final EntityManager entityManager;  

    public TourRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @ReadOnly 
    public Optional<Tour> findById(UUID id) {
        return Optional.ofNullable(entityManager.find(Tour.class, id));
    }

    @Override
    @TransactionalAdvice
    public UUID persist(Tour tour) {
        if (entityManager.contains(tour)) {
            entityManager.persist(tour);
        } else {
            entityManager.merge(tour);
        }
        
        return tour.getId();
    }

    @Override
    @TransactionalAdvice
    public void deleteById(UUID id) {
        this.findById(id).ifPresent(entityManager::remove);
    }

    @Override
    @ReadOnly 
    public List<Tour> findAll() {
        String qlString = "SELECT t FROM Tour as t";
        TypedQuery<Tour> query = entityManager.createQuery(qlString, Tour.class);

        return query.getResultList();
    }
}
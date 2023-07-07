package cz.krvotok.tdf.adapter.out.persistance.h2;

import io.micronaut.transaction.annotation.ReadOnly;
import jakarta.inject.Singleton;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import cz.krvotok.tdf.domain.model.aggregate.Search;
import cz.krvotok.tdf.domain.repository.SearchRepository;
import io.micronaut.transaction.annotation.TransactionalAdvice;

@Singleton 
public class SearchRepositoryImpl implements SearchRepository {

    private final EntityManager entityManager;  

    public SearchRepositoryImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @ReadOnly 
    public Optional<Search> findById(UUID id) {
        return Optional.ofNullable(entityManager.find(Search.class, id));
    }

    @Override
    @TransactionalAdvice
    public UUID persist(Search search) {
        if (entityManager.contains(search)) {
            entityManager.persist(search);
        } else {
            entityManager.merge(search);
        }
        
        return search.getId();
    }

    @Override
    @TransactionalAdvice
    public void deleteById(UUID id) {
        this.findById(id).ifPresent(entityManager::remove);
    }

    @Override
    @ReadOnly 
    public List<Search> findAll() {
        String qlString = "SELECT p FROM Search as p";
        TypedQuery<Search> query = entityManager.createQuery(qlString, Search.class);

        return query.getResultList();
    }
}
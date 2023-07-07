package cz.krvotok.tdf.application.handler;

import java.util.UUID;

import cz.krvotok.tdf.domain.model.aggregate.Search;

import cz.krvotok.tdf.application.command.CreateSearchCommand;
import cz.krvotok.tdf.application.service.RouteSearcher;
import cz.krvotok.tdf.domain.repository.SearchRepository;
import jakarta.inject.Singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Singleton
public class CreateSearchHandler {
    private static final Logger LOG = LoggerFactory.getLogger(RouteSearcher.class);

    private final SearchRepository repository;
    private final RouteSearcher routeSearcher;

    public CreateSearchHandler(SearchRepository repository, RouteSearcher routeSearcher) {
        this.routeSearcher = routeSearcher;
        this.repository = repository;
    }

    public UUID handle(CreateSearchCommand command) {
        Search search = new Search(
            command.getCheckpoints(),
            command.getStartCheckpointIdx(),
            command.getFinishCheckpointIdx(),
            command.getMaxDistance(),
            command.getMaxAscend(),
            command.getNoOfCheckpoints()
        );

        LOG.info("Persisting ...");
        UUID searchId = this.repository.persist(search);
        LOG.info("Persisted ...");

       this.routeSearcher.searchForRoutes(searchId);

        return searchId;
    }
}
package cz.krvotok.tdf.application.handler;

import java.util.UUID;

import cz.krvotok.tdf.domain.model.aggregate.Search;

import cz.krvotok.tdf.application.command.CreateSearchCommand;
import cz.krvotok.tdf.application.service.SearchService;
import cz.krvotok.tdf.domain.repository.SearchRepository;
import jakarta.inject.Singleton;

@Singleton
public class CreateSearchHandler {
    private final SearchRepository repository;
    private final SearchService searchService;

    public CreateSearchHandler(SearchRepository repository, SearchService searchService) {
        this.searchService = searchService;
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

        UUID searchId = this.repository.persist(search);

        this.searchService.completeSearch(searchId);

        return searchId;
    }
}
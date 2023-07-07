package cz.krvotok.tdf.adapter.in.rest.controller;

import java.net.URI;
import java.util.UUID;

import javax.validation.Valid;

import cz.krvotok.tdf.application.command.CreateSearchCommand;

import cz.krvotok.tdf.domain.model.aggregate.Search;
import cz.krvotok.tdf.application.handler.CreateSearchHandler;
import cz.krvotok.tdf.domain.repository.SearchRepository;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Controller("/searches")
public class SearchController {
    private final CreateSearchHandler createSearchHandler;
    private final SearchRepository searchRepository;

    public SearchController(CreateSearchHandler createSearchHandler, SearchRepository searchRepository) {
        this.createSearchHandler = createSearchHandler;
        this.searchRepository = searchRepository;
    }

    @Operation(summary = "Returns existing Search")
    @ApiResponse(responseCode = "200", description = "Routes search found")
    @ApiResponse(responseCode = "404", description = "Routes search not found")
    @Get("/{id}") 
    public Search getSearch(UUID id) {
        return this.searchRepository
                .findById(id)
                .orElse(null); 
    }
    
    @Operation(summary = "Creates and starts new search.")
    @Post(uri="/", produces=MediaType.APPLICATION_JSON) 
    public HttpResponse<Search> createSearch(@Body @Valid CreateSearchCommand command) {
        UUID searchId = this.createSearchHandler.handle(command);

        return HttpResponse
                .accepted(location(searchId));
    }

    private URI location(UUID id) {
        return URI.create("/searches/" + id.toString());
    }

}
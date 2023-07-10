package cz.krvotok.tdf.adapter.in.rest.controller;

import java.net.URI;
import java.util.UUID;

import javax.validation.Valid;

import cz.krvotok.tdf.application.command.CreateTourCommand;

import cz.krvotok.tdf.domain.model.aggregate.Tour;
import cz.krvotok.tdf.application.handler.CreateTourHandler;
import cz.krvotok.tdf.domain.repository.TourRepository;

import io.micronaut.http.HttpResponse;
import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Post;
import io.micronaut.http.MediaType;
import io.micronaut.http.annotation.Body;
import io.micronaut.http.annotation.Get;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@Controller("/tours")
public class TourController {
    private final CreateTourHandler createTourHandler;
    private final TourRepository tourRepository;

    public TourController(CreateTourHandler createTourHandler, TourRepository tourRepository) {
        this.createTourHandler = createTourHandler;
        this.tourRepository = tourRepository;
    }

    @Operation(summary = "Returns existing Tour")
    @ApiResponse(responseCode = "200", description = "Tour found")
    @ApiResponse(responseCode = "404", description = "Tour not found")
    @Get("/{id}") 
    public Tour getTour(UUID id) {
        return this.tourRepository
                .findById(id)
                .orElse(null); 
    }
    
    @Operation(summary = "Creates tour.")
    @Post(uri="/", produces=MediaType.APPLICATION_JSON) 
    public HttpResponse<Tour> createTour(@Body @Valid CreateTourCommand command) {
        UUID tourId = this.createTourHandler.handle(command);

        return HttpResponse
                .accepted(location(tourId));
    }

    private URI location(UUID id) {
        return URI.create("/tours/" + id.toString());
    }
}
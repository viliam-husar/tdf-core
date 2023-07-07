package cz.krvotok.tdf.adapter.in.rest.controller;

import cz.krvotok.tdf.domain.model.aggregate.TourDeFelvidek;
import cz.krvotok.tdf.domain.repository.TourDeFelvidekRepository;

import io.micronaut.http.annotation.Controller;
import io.micronaut.http.annotation.Get;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;

@Controller("/tdfs")
@Tag(name = "tdf")
public class TdfController {
    private final TourDeFelvidekRepository tdfRepository;

    public TdfController(TourDeFelvidekRepository tdfRepository) {
        this.tdfRepository = tdfRepository;
    }

    @Operation(summary = "Returns TDF checkpoints for given year")
    @ApiResponse(responseCode = "200", description = "TDF found")
    @ApiResponse(responseCode = "404", description = "TDF not found")
    @Get("/{year}") 
    public TourDeFelvidek getTdf(int year) {
        return this.tdfRepository
                .findByYear(year)
                .orElse(null); 
    }
}
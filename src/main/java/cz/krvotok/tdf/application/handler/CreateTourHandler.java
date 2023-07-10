package cz.krvotok.tdf.application.handler;

import java.util.UUID;

import cz.krvotok.tdf.domain.model.aggregate.Tour;

import cz.krvotok.tdf.application.command.CreateTourCommand;
import cz.krvotok.tdf.application.service.TourService;
import cz.krvotok.tdf.domain.repository.TourRepository;
import jakarta.inject.Singleton;

@Singleton
public class CreateTourHandler {
    private final TourRepository tourRepository;
    private final TourService tourService;

    public CreateTourHandler(TourRepository tourRepository, TourService tourService) {
        this.tourRepository = tourRepository;
        this.tourService = tourService;
    }

    public UUID handle(CreateTourCommand command) {
        Tour tour = new Tour(
            command.getCheckpoints()
        );

        UUID tourId = this.tourRepository.persist(tour);

        this.tourService.completeTour(tourId);

        return tourId;
    }
}
package cz.krvotok.tdf.application.service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cz.krvotok.tdf.domain.model.aggregate.Tour;
import cz.krvotok.tdf.domain.model.valueobject.TourRestpoint;
import cz.krvotok.tdf.domain.repository.TourRepository;
import io.micronaut.scheduling.annotation.Async;
import jakarta.inject.Singleton;

@Singleton
public class TourService {
    private final TourRepository tourRepository;
    private final NavigationService navigationService;

    public TourService(TourRepository tourRepository, NavigationService navigationService) {
        this.tourRepository = tourRepository;
        this.navigationService = navigationService;
    }

    @Async
    public void completeTour(UUID tourId) {
        Tour tour = this.tourRepository.findById(tourId).orElse(null);
        tour.setStatus(Tour.STATUS_IN_PROGRESS);

        this.addTourRestpoints(tour);

        tour.setStatus(Tour.STATUS_IN_PROGRESS);
        this.tourRepository.persist(tour);
    }

    public void addTourRestpoints(Tour tour) {
        List<List<TourRestpoint>> availableRestpoints = new ArrayList<>();

        // We don't need to calculate restopoints from last CP
        for (int i = 0; i < (tour.getCheckpoints().size() - 1); i++) {
            List<TourRestpoint> tourRestpoints = new ArrayList<>();
            TourRestpoint tr = new TourRestpoint();
            tr.setName("aaa");
            tr.setDescription("sss");
            tr.setLatitude(0);
            tr.setLongitude(0);
            tr.setAdditionalDistance(1);
            tr.setAdditionalAscend(2);

            tourRestpoints.add(tr);
            availableRestpoints.add(i, tourRestpoints);         
        }

        tour.setAvailableRestpoints(availableRestpoints);
        this.tourRepository.persist(tour);

    }
}


package cz.krvotok.tdf.application.service;

public interface NavigationService {
    int[] findPathMetadata(double fromLat, double fromLon, double toLat, double toLon);
}


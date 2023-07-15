package cz.krvotok.tdf.application.service;

public interface NavigationService {
    int[] getPathMetadata(double fromLat, double fromLon, double toLat, double toLon);
    
    int getPointAltitude(double lat, double lon);
    // Point[] getPathPoints(double fromLat, double fromLon, double toLat, double toLon);
}


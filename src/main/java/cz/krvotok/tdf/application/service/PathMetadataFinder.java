package cz.krvotok.tdf.application.service;

public interface PathMetadataFinder {
    int[] findPathMetadata(double fromLat, double fromLon, double toLat, double toLon);
}


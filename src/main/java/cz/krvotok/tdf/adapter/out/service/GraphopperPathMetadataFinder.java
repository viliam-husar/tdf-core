package cz.krvotok.tdf.adapter.out.service;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.ResponsePath;

import java.util.Locale;

import cz.krvotok.tdf.application.service.PathMetadataFinder;
import io.micronaut.cache.annotation.Cacheable;
import jakarta.inject.Singleton;

@Singleton
public class GraphopperPathMetadataFinder implements PathMetadataFinder {

    final private GraphHopper hopper;
    
    public GraphopperPathMetadataFinder(GraphHopper hopper) {
        this.hopper = hopper;
    }

    @Override
    @Cacheable(cacheNames = "paths", parameters = {"fromLat", "fromLon", "toLat", "toLon"})
    public int[] findPathMetadata(double fromLat, double fromLon, double toLat, double toLon) {

        int[] path = new int[3];

        GHRequest req = new GHRequest(fromLat, fromLon, toLat, toLon)
            .setProfile("bike")
            .setLocale(Locale.US);

        GHResponse rsp = this.hopper.route(req);

        if (rsp.hasErrors())
            throw new RuntimeException(rsp.getErrors().toString());

        ResponsePath rspPath = rsp.getBest();

        path[0] = (int) rspPath.getDistance();
        path[1] = (int) rspPath.getAscend();
        path[2] = (int) rspPath.getDescend();

        return path;
    }
}
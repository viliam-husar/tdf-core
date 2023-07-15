package cz.krvotok.tdf.adapter.out.service;

import com.graphhopper.GHRequest;
import com.graphhopper.GHResponse;
import com.graphhopper.GraphHopper;
import com.graphhopper.ResponsePath;

import java.util.Locale;

import com.graphhopper.util.PointList;

import cz.krvotok.tdf.application.service.NavigationService;
import io.micronaut.cache.annotation.Cacheable;
import jakarta.inject.Singleton;

@Singleton
public class GraphHopperNavigationService implements NavigationService {

    final private GraphHopper hopper;
    
    public GraphHopperNavigationService(GraphHopper hopper) {
        this.hopper = hopper;
    }

    @Override
    @Cacheable(cacheNames = "paths", parameters = {"fromLat", "fromLon", "toLat", "toLon"})
    public int[] getPathMetadata(double fromLat, double fromLon, double toLat, double toLon) {

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

    @Override
    @Cacheable(cacheNames = "altitudes", parameters = {"lat", "lon"})
    public int getPointAltitude(double lat, double lon) {
        GHRequest req = new GHRequest(lat, lon, lat, lon)
            .setProfile("bike")
            .setLocale(Locale.US);

        GHResponse rsp = hopper.route(req);

        if(rsp.hasErrors()) {
            return -1;
            // handle errors
        } else {
            PointList pointList = rsp.getBest().getPoints();
            
            return (int) pointList.getEle(0);
        }
    }

    // @Override
    // @Cacheable(cacheNames = "paths", parameters = {"fromLat", "fromLon", "toLat", "toLon"})
    // public Point[] getPathPoints(double fromLat, double fromLon, double toLat, double toLon) {

    //     return new Object();
    // }
}
package cz.krvotok.tdf.adapter.out.service;

import java.nio.file.Paths;

import com.graphhopper.GraphHopper;
import com.graphhopper.config.Profile;
import com.graphhopper.reader.dem.SRTMProvider;

import io.micronaut.context.annotation.Factory;
import jakarta.inject.Singleton;

@Factory
public class HopperFactory {

    @Singleton
    @SuppressWarnings("unused")
    GraphHopper graphHopper() {
        GraphHopper hopper = new GraphHopper();
        hopper.setOSMFile(Paths.get("src/main/resources/maps/tdf.osm.pbf").toAbsolutePath().toString());
        hopper.setGraphHopperLocation("target/routing-graph-cache");
        hopper.setElevationProvider(new SRTMProvider());
        hopper.setElevation(true);
        hopper.setProfiles(new Profile("bike").setVehicle("bike").setWeighting("fastest").setTurnCosts(false));
        hopper.importOrLoad();        

        return hopper;
    }
}
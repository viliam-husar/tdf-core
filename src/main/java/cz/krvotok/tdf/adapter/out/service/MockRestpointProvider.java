package cz.krvotok.tdf.adapter.out.service;

import java.util.ArrayList;
import java.util.List;

import cz.krvotok.tdf.application.service.RestpointProvider;
import cz.krvotok.tdf.domain.model.valueobject.Checkpoint;
import cz.krvotok.tdf.domain.model.valueobject.Restpoint;
import jakarta.inject.Singleton;

@Singleton
public class MockRestpointProvider implements RestpointProvider {

    @Override
    public List<Restpoint> getRestpoints(Checkpoint fromCheckpoint, Checkpoint toCheckpoint) {
        List<Restpoint> rps = new ArrayList<>();
        
        Restpoint rp = new Restpoint();
            rp.setName("aaa");
            rp.setDescription("sss");
            rp.setLatitude(0);
            rp.setLongitude(0);

        rps.add(rp);

        return rps;
    }
}
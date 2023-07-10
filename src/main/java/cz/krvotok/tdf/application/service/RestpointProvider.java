package cz.krvotok.tdf.application.service;

import java.util.List;

import cz.krvotok.tdf.domain.model.valueobject.Checkpoint;
import cz.krvotok.tdf.domain.model.valueobject.Restpoint;

public interface RestpointProvider {
    List<Restpoint> getRestpoints(Checkpoint fromCheckpoint, Checkpoint toCheckpoint);
}


package cz.krvotok.tdf.domain.model.aggregate;

import java.util.List;

import cz.krvotok.tdf.domain.model.valueobject.Checkpoint;
import cz.krvotok.tdf.domain.model.valueobject.Restpoint;

public class Tour {
    private List<Checkpoint> checkpoints;
    private List<Restpoint>[] availableRestpoints;
    private int[] selectedRestpointsIdxs;
}
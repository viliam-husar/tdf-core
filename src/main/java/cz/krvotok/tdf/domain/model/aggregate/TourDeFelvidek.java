package cz.krvotok.tdf.domain.model.aggregate;

import java.util.List;

import cz.krvotok.tdf.domain.model.valueobject.Checkpoint;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class TourDeFelvidek {
    private List<Checkpoint> checkpoints;
    private int startCheckpointIdx;
    private int finishCheckopintIdx;
    private int year;

    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(List<Checkpoint> checkpoints) {
        this.checkpoints = checkpoints;
    }

    public int getStartCheckpointIdx() {
        return this.startCheckpointIdx;
    }

    public void setStartCheckpointIdx(int startCheckpointIdx) {
        this.startCheckpointIdx = startCheckpointIdx;
    }

    public int getFinishCheckopintIdx() {
        return this.finishCheckopintIdx;
    }

    public void setFinishCheckopintIdx(int finishCheckopintIdx) {
        this.finishCheckopintIdx = finishCheckopintIdx;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }
}

package cz.krvotok.tdf.application.command;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import io.micronaut.serde.annotation.Serdeable;

import javax.validation.constraints.NotNull;

import cz.krvotok.tdf.domain.model.valueobject.Checkpoint;

@Serdeable
final public class CreateSearchCommand {
    @NotEmpty
    private List<Checkpoint> checkpoints;
        
    @NotNull
    private int startCheckpointIdx;

    @NotNull
    private int finishCheckpointIdx;

    @NotNull
    private int maxDistance;

    @NotNull
    private int maxAscend;

    @NotNull
    private int noOfCheckpoints;

    public List<Checkpoint> getCheckpoints() {
        return this.checkpoints;
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

    public int getFinishCheckpointIdx() {
        return this.finishCheckpointIdx;
    }

    public void setFinishCheckpointIdx(int finishCheckpointIdx) {
        this.finishCheckpointIdx = finishCheckpointIdx;
    }

    public int getMaxDistance() {
        return this.maxDistance;
    }

    public void setMaxDistance(int maxDistance) {
        this.maxDistance = maxDistance;
    }

    public int getMaxAscend() {
        return this.maxAscend;
    }

    public void setMaxAscend(int maxAscend) {
        this.maxAscend = maxAscend;
    }

    public int getNoOfCheckpoints() {
        return this.noOfCheckpoints;
    }

    public void setNoOfCheckpoints(int noOfCheckpoints) {
        this.noOfCheckpoints = noOfCheckpoints;
    }
}
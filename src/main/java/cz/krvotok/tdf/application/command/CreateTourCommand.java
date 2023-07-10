package cz.krvotok.tdf.application.command;

import java.util.List;

import javax.validation.constraints.NotEmpty;

import io.micronaut.serde.annotation.Serdeable;

import cz.krvotok.tdf.domain.model.valueobject.Checkpoint;

@Serdeable
final public class CreateTourCommand {
    @NotEmpty
    private List<Checkpoint> checkpoints;
        

    public List<Checkpoint> getCheckpoints() {
        return this.checkpoints;
    }

    public void setCheckpoints(List<Checkpoint> checkpoints) {
        this.checkpoints = checkpoints;
    }
}
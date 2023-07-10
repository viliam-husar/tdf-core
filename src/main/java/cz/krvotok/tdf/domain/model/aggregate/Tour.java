package cz.krvotok.tdf.domain.model.aggregate;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import cz.krvotok.tdf.adapter.out.persistance.h2.IntArrayConverter;
import cz.krvotok.tdf.adapter.out.persistance.h2.ListCheckpointConverter;
import cz.krvotok.tdf.adapter.out.persistance.h2.ListTourRestpointListConverter;
import cz.krvotok.tdf.domain.model.valueobject.Checkpoint;
import cz.krvotok.tdf.domain.model.valueobject.TourRestpoint;
import io.micronaut.serde.annotation.Serdeable;

@Serdeable
@Entity
@Table(name="tour")
public final class Tour {
    public static final String STATUS_NEW = "new";
    public static final String STATUS_IN_PROGRESS = "in_progress";
    public static final String STATUS_READY = "ready";

    @Id
    private UUID id;

    @NotEmpty
    @Column(name = "checkpoints", nullable = false, columnDefinition="CLOB")
    @Convert(converter = ListCheckpointConverter.class)
    private List<Checkpoint> checkpoints;

    @Column(name = "availableRestpoints", nullable = false, columnDefinition="CLOB")
    @Convert(converter = ListTourRestpointListConverter.class)
    private List<List<TourRestpoint>> availableRestpoints;

    @NotEmpty
    @Column(name = "selectedRestpointsIdx", nullable = false, columnDefinition="CLOB")
    @Convert(converter = IntArrayConverter.class)
    private int[] selectedRestpointsIdxs;

    @NotNull
    @Column(name = "status", nullable = false)
    private String status;

    public Tour() {}

    public Tour(
        List<Checkpoint> checkpoints
    ) {
        this.id = UUID.randomUUID();
        this.checkpoints = checkpoints;
        this.availableRestpoints = new ArrayList<>();
        this.selectedRestpointsIdxs = new int[this.checkpoints.size()];
        this.status = Tour.STATUS_NEW;
    }

    public UUID getId() {
        return this.id;
    }

    public List<Checkpoint> getCheckpoints() {
        return this.checkpoints;
    }

    public int[] getSelectedRestpointsIdxs() {
        return this.selectedRestpointsIdxs;
    }

    public void setAvailableRestpoints(List<List<TourRestpoint>> availableRestpoints) {
        this.availableRestpoints = availableRestpoints;
    }

    public List<List<TourRestpoint>> getAvailableRestpoints() {
        return this.availableRestpoints;
    }

    public void selectTourRestpoint(int checkpointIdx, int restpointIdx) {
        this.selectedRestpointsIdxs[checkpointIdx] = restpointIdx;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
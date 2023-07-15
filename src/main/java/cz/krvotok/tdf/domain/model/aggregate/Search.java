package cz.krvotok.tdf.domain.model.aggregate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

import io.micronaut.serde.annotation.Serdeable;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import cz.krvotok.tdf.adapter.out.persistance.h2.LongArrayConverter;
import cz.krvotok.tdf.adapter.out.persistance.h2.ListCheckpointConverter;
import cz.krvotok.tdf.adapter.out.persistance.h2.ListRouteConverter;
import cz.krvotok.tdf.domain.model.valueobject.Checkpoint;
import cz.krvotok.tdf.domain.model.valueobject.Route;

@Serdeable
@Entity
@Table(name="search")
public final class Search {
    public static final String STATUS_NEW = "new";
    public static final String STATUS_IN_PROGRESS = "in_progress";
    public static final String STATUS_READY = "ready";

    public static final int STATS_ALL = 0;
    public static final int STATS_SKIPPED = 1;
    public static final int STATS_FOUND = 2;


    @Id
    private UUID id;

    @NotEmpty
    @Column(name = "checkpoints", nullable = false, columnDefinition="CLOB")
    @Convert(converter = ListCheckpointConverter.class)
    private List<Checkpoint> checkpoints;

    @Convert(converter = ListRouteConverter.class)
    @Column(name = "routes", columnDefinition="CLOB")
    private List<Route> routes;

    @NotNull
    @Column(name = "startCheckpointIdx", nullable = false)
    private int startCheckpointIdx;

    @NotNull
    @Column(name = "finishCheckpointIdx", nullable = false)
    private int finishCheckpointIdx;

    @NotNull
    @Max(value = 1000000, message = "Really? 1000km in 40 hours?")
    @Min(value = 0, message = "Distance must be postive as you are.")
    @Column(name = "maxDistance", nullable = false)
    private int maxDistance;

    @NotNull
    @Column(name = "maxAscend", nullable = false)
    private int maxAscend;

    @NotNull
    @Max(value = 15, message = "There are some limitations...")
    @Min(value = 5, message = "At least 5 CPs are required by this algorithm...")
    @Column(name = "noOfCheckpoints", nullable = false)
    private int noOfCheckpoints;
    
    @NotEmpty
    @Column(name = "stats", nullable = false, columnDefinition="CLOB")
    @Convert(converter = LongArrayConverter.class)
    private long[] stats;

    @NotNull
    @Column(name = "status", nullable = false)
    private String status;

    // @Column(name = "progress", columnDefinition="CLOB" nullable = false);
    // private Progress progress;

    public Search() {}

    public Search(
        List<Checkpoint> checkpoints,
        int startCheckpointIdx,
        int finishCheckpointIdx,
        int maxDistance,
        int maxAscend,
        int noOfCheckpoints
    ) {
        this.id = UUID.randomUUID();
        this.status = Search.STATUS_NEW;
        this.checkpoints = checkpoints;
        this.startCheckpointIdx = startCheckpointIdx;
        this.finishCheckpointIdx = finishCheckpointIdx;
        this.maxDistance = maxDistance;
        this.maxAscend = maxAscend;
        this.noOfCheckpoints = noOfCheckpoints;
        // 0 - all possible roites
        // 1 - skipped routes
        // 2 - found routes
        this.stats = new long[3];
        this.stats[0] = (long) Math.pow(6, noOfCheckpoints);
        this.routes = new ArrayList<>();
    }

    public UUID getId() {
        return this.id;
    }

    public List<Checkpoint> getCheckpoints() {
        return this.checkpoints;
    }

    public int getStartCheckpointIdx() {
        return this.startCheckpointIdx;
    }

    public int getFinishCheckpointIdx() {
        return this.finishCheckpointIdx;
    }

    public List<Route> getRoutes() {
        return this.routes;
    }

    public boolean addRoute(Route route) {
        // If the list is not full yet, add the new route.
        // If the list is full and the new route is easier than the hardest route, add the new route.
        if (routes.size() < 50 || route.getDistance() < routes.get(routes.size() - 1).getDistance()) {
            routes.add(route);
            routes.sort(Comparator.comparingInt(Route::getDistance));

            // If there are more than 20 routes, remove the most difficult
            if (routes.size() > 50) {
                routes.remove(routes.size() - 1);
            }

            return true;
        }

        return false;
    }   

    public int getMaxDistance() {
        return this.maxDistance;
    }

    public int getMaxAscend() {
        return this.maxAscend;
    }

    public int getNoOfCheckpoints() {
        return this.noOfCheckpoints;
    }

    public long[] getStats() {
        return this.stats;
    }

    public void increaseStats(int statType, long add) {
        this.stats[statType] = this.stats[statType] + add;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

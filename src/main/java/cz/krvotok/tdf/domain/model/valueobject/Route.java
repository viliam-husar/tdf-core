package cz.krvotok.tdf.domain.model.valueobject;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public final class Route {
    private int[] checkpointsIdxs;
    private int distance;
    private int ascend;
    private double ascendRatio;
    
    public Route() {}
    
    public Route(int[] checkpointsIdxs, int distance, int ascend) {
        this.setCheckpointsIdxs(checkpointsIdxs);
        this.setDistance(distance); // 100
        this.setAscend(ascend); // 4000 10000 
        this.setAscendRatio(distance / ascend);
    }

    public int[] getCheckpointsIdxs() {
        return this.checkpointsIdxs;
    }

    public void setCheckpointsIdxs(int[] checkpointsIdxs) {
        this.checkpointsIdxs = checkpointsIdxs;
    }

    public int getDistance() {
        return this.distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getAscend() {
        return this.ascend;
    }

    public void setAscend(int ascend) {
        this.ascend = ascend;
    }

    public double getAscendRatio() {
        return ascendRatio;
    }

    public void setAscendRatio(double ascendRatio) {
        this.ascendRatio = ascendRatio;
    }
}

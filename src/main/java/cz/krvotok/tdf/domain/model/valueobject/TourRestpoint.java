package cz.krvotok.tdf.domain.model.valueobject;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class TourRestpoint extends Restpoint {
    private int additionalDistance;
    private int additionalAscend;

    public int getAdditionalDistance() {
        return additionalDistance;
    }

    public void setAdditionalDistance(int additionalDistance) {
        this.additionalDistance = additionalDistance;
    }

    public int getAdditionalAscend() {
        return additionalAscend;
    }

    public void setAdditionalAscend(int additionalAscend) {
        this.additionalAscend = additionalAscend;
    }

    
}
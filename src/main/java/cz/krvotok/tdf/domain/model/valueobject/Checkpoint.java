package cz.krvotok.tdf.domain.model.valueobject;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public final class Checkpoint extends Waypoint {
    @Override
    public String getType() {
        return "CHECKPOINT";
    }
}
package cz.krvotok.tdf.domain.model.valueobject;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public class Restpoint extends Waypoint {
    @Override
    public String getType() {
        return "RESTPOINT";
    }
}
package cz.krvotok.tdf.domain.model.valueobject;

import io.micronaut.serde.annotation.Serdeable;

@Serdeable
public final class Progress {
    int max; // No of CP ^ 6
    int skipped; 
    int found; // 
    public Progress() {}
}
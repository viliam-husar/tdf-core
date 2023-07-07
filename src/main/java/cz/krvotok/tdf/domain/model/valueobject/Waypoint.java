package cz.krvotok.tdf.domain.model.valueobject;

import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonIgnore;

public abstract class Waypoint {
    private String name;
    private String description;
    private double latitude;
    private double longitude;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Transient
    @JsonIgnore
    abstract public String getType();
}
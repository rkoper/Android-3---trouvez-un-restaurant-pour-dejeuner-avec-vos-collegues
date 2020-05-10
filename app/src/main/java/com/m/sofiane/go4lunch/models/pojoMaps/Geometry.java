package com.m.sofiane.go4lunch.models.pojoMaps;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * created by Sofiane M. 2020-01-31
 */
public class Geometry {
    @SerializedName("location")
    @Expose
    private Location location;

    /**
     *
     * @return
     * The location
     */
    public Location getLocation() {
        return location;
    }

    /**
     *
     * @param location
     * The location
     */
    public void setLocation(Location location) {
        this.location = location;
    }
}

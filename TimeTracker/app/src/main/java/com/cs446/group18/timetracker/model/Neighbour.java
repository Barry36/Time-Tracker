package com.cs446.group18.timetracker.model;

import com.cs446.group18.timetracker.constants.QuadTreeConstant;

public class Neighbour {
    private long id;
    private double latitude;
    private double longitude;

    public Neighbour(long id, double latitude, double longitude) {
        this.id = id;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getId() {
        return id;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public String toString() {
        return "Neighbour{" +
                "id=" + id +
                ", latitude=" + denormalizeLatitude(latitude) +
                ", longitude=" + denormalizeLongitude(longitude) +
                '}';
    }

    public double denormalizeLatitude(double latitude) {
        return latitude - QuadTreeConstant.NORMALIZE_Y;
    }

    public double denormalizeLongitude(double longitude) {
        return longitude - QuadTreeConstant.NORMALIZE_X;
    }
}

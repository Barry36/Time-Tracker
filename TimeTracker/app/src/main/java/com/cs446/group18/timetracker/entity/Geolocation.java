package com.cs446.group18.timetracker.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "geolocation_table",
        foreignKeys = {@ForeignKey(entity = TimeEntry.class, parentColumns = {"time_entry_id"}, childColumns = {"time_entry_id"})},
        indices = {@Index("time_entry_id")}
)
public class Geolocation {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "geolocation_id")
    private long geolocationId;

    @ColumnInfo(name = "time_entry_id")
    private long timeEntryId;

    private double latitude;

    private double longitude;

    public Geolocation(long timeEntryId, double latitude, double longitude) {
        this.timeEntryId = timeEntryId;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public long getGeolocationId() {
        return geolocationId;
    }

    public void setGeolocationId(long geolocationId) {
        this.geolocationId = geolocationId;
    }

    public long getTimeEntryId() {
        return timeEntryId;
    }

    public void setTimeEntryId(long timeEntryId) {
        this.timeEntryId = timeEntryId;
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
}

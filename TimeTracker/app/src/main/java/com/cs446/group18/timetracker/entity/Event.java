package com.cs446.group18.timetracker.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "event_table")
public class Event {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "event_id")
    private long eventId;

    @ColumnInfo(name = "event_name")
    private String eventName;

    @ColumnInfo(name = "event_description")
    private String description;

    @ColumnInfo(name = "icon")
    private Integer icon;

    public Event(String eventName, String description, int icon) {
        this.eventName = eventName;
        this.description = description;
        this.icon = icon;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getIcon() {
        return icon;
    }

    public void setIcon(Integer icon) {
        this.icon = icon;
    }
}

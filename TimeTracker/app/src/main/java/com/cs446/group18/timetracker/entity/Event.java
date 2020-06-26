package com.cs446.group18.timetracker.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Event {
    @PrimaryKey(autoGenerate = true)
    private long eventId;

    private String name;

    private String description;

    public Event(String name, String description) {
        this.name = name;
        this.description = description;
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventId=" + eventId +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public long getEventId() {
        return eventId;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }
}

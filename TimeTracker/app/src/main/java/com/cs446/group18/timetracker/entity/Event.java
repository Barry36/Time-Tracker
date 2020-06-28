package com.cs446.group18.timetracker.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "event_table",
        foreignKeys = {@ForeignKey(entity = Project.class, parentColumns = {"project_id"}, childColumns = {"project_id"})},
        indices = {@Index("project_id")}
)
public class Event {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "event_id")
    private long eventId;

    @ColumnInfo(name = "project_id")
    public long projectId;

    @ColumnInfo(name = "event_name")
    private String eventName;

    @ColumnInfo(name = "event_description")
    private String description;

    public Event(long projectId, String eventName, String description) {
        this.projectId = projectId;
        this.eventName = eventName;
        this.description = description;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
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
}

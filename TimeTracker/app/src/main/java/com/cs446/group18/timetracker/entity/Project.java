package com.cs446.group18.timetracker.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "project_table",
        foreignKeys = {@ForeignKey(entity = Event.class, parentColumns = {"project_id"}, childColumns = {"project_id"})},
        indices = {@Index("project_id")}
)
public class Project {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "project_id")
    private long projectId;

    @ColumnInfo(name = "event_id")
    private long eventId;

    @ColumnInfo(name = "project_name")
    private String name;

    @ColumnInfo(name = "project_description")
    private String description;

    @ColumnInfo(name = "project_color")
    private String color;

    public Project(long eventId, String name, String description, String color) {
        this.eventId = eventId;
        this.name = name;
        this.description = description;
        this.color = color;
    }

    public long getProjectId() {
        return projectId;
    }

    public void setProjectId(long projectId) {
        this.projectId = projectId;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

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

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}

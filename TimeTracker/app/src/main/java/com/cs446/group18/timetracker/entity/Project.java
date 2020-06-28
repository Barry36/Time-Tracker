package com.cs446.group18.timetracker.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "project_table")
public class Project {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "project_id")
    private long projectId;

    @ColumnInfo(name = "project_name")
    private String name;

    @ColumnInfo(name = "project_description")
    private String description;

    @ColumnInfo(name = "project_color")
    private String color;

    public Project(String name, String description, String color) {
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

package com.cs446.group18.timetracker.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "goal_table",
        foreignKeys = {@ForeignKey(entity = Event.class, onDelete = ForeignKey.CASCADE, parentColumns = {"event_id"}, childColumns = {"event_id"})},
        indices = {@Index("event_id")}
)
public class Goal {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "goal_id")
    private long goalId;

    @ColumnInfo(name = "event_id")
    private long eventId;

    @ColumnInfo(name = "goal_name")
    private String name;

    @ColumnInfo(name = "goal_description")
    private String description;

    @ColumnInfo(name = "goal_progress")
    private int progressValue;

    @ColumnInfo(name = "goal_target")
    private int targetValue;

    public Goal(long eventId, String name, String description, int progressValue, int targetValue) {
        this.eventId = eventId;
        this.name = name;
        this.progressValue = progressValue;
        this.description = description;
        this.targetValue = targetValue;
    }

    public long getGoalId() {
        return goalId;
    }

    public void setGoalId(long goalId) {
        this.goalId = goalId;
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

    public int getProgressValue() {
        return progressValue;
    }

    public void setProgressValue(int progressValue) {
        this.progressValue = progressValue;
    }

    public int getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(int targetValue) {
        this.targetValue = targetValue;
    }
}

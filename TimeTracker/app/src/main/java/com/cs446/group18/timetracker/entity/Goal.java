package com.cs446.group18.timetracker.entity;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

@Entity(
        tableName = "goal_table",
        foreignKeys = {@ForeignKey(entity = Event.class, parentColumns = {"event_id"}, childColumns = {"event_id"})},
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

    @ColumnInfo(name = "goal_target")
    private double targetValue;

    public Goal(long eventId, String name, String description, double targetValue) {
        this.eventId = eventId;
        this.name = name;
        this.targetValue = targetValue;
        this.description = description;
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

    public double getTargetValue() {
        return targetValue;
    }

    public void setTargetValue(double targetValue) {
        this.targetValue = targetValue;
    }
}

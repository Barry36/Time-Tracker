package com.cs446.group18.timetracker.entity;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "entry_table")
public class TimeEntry {
    @PrimaryKey(autoGenerate = true)
    private long entryId;

    private Date startTime;

    private Date endTime;

    private Long duration;

    public TimeEntry(Date startTime, Date endTime, Long duration) {
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
    }

    @Override
    public String toString() {
        return "TimeEntry{" +
                "entryId=" + entryId +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", duration=" + duration +
                '}';
    }

    public void setEntryId(long entryId) {
        this.entryId = entryId;
    }

    public long getEntryId() {
        return entryId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public Long getDuration() {
        return duration;
    }
}

package com.cs446.group18.timetracker.entity;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Index;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(
        tableName = "time_entry_table",
        foreignKeys = {@ForeignKey(entity = Event.class,  parentColumns = {"event_id"}, childColumns = {"event_id"})},
        indices = {@Index("event_id")}
        )
public class TimeEntry {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "time_entry_id")
    private long timeEntryId;

    @ColumnInfo(name = "event_id")
    private long eventId;

    @ColumnInfo(name = "start_time")
    private Date startTime;

    @ColumnInfo(name = "end_time")
    private Date endTime;

    private Long duration;

    public TimeEntry(long eventId, Date startTime, Date endTime, Long duration) {
        this.eventId = eventId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
    }

    public long getTimeEntryId() {
        return timeEntryId;
    }

    public void setTimeEntryId(long timeEntryId) {
        this.timeEntryId = timeEntryId;
    }

    public long getEventId() {
        return eventId;
    }

    public void setEventId(long eventId) {
        this.eventId = eventId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }
}

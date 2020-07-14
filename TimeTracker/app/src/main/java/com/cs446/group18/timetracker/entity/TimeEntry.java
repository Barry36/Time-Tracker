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

    private long duration;

    public TimeEntry(long eventId, Date startTime, Date endTime, long duration) {
        this.eventId = eventId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.duration = duration;
    }

    public String myFunction(){return "Barry is Awesome";}
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

    public String getStartTimeStr(){return startTime.toString();}

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public String getEndTimeStr(){return endTime.toString();}

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public long getDuration() {
        return duration;
    }

    public String getDurationStr(){return Long.toString(duration);}

    public void setDuration(long duration) {
        this.duration = duration;
    }
}

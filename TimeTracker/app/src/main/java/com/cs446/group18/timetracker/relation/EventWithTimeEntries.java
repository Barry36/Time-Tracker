package com.cs446.group18.timetracker.relation;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.cs446.group18.timetracker.entity.Event;
import com.cs446.group18.timetracker.entity.TimeEntry;

import java.util.ArrayList;
import java.util.List;

public class EventWithTimeEntries {
    @Embedded
    private Event event;

    @Relation(parentColumn = "event_id", entityColumn = "time_entry_id")
    private List<TimeEntry> timeEntries = new ArrayList<>();

    public Event getEvent() {
        return event;
    }

    public List<TimeEntry> getTimeEntries() {
        return timeEntries;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setTimeEntries(List<TimeEntry> timeEntries) {
        this.timeEntries = timeEntries;
    }
}

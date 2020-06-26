package com.cs446.group18.timetracker.relation;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.cs446.group18.timetracker.entity.Event;
import com.cs446.group18.timetracker.entity.TimeEntry;

import java.util.List;

public class EventWithTimeEntries {
    @Embedded
    public Event event;

    @Relation(
            parentColumn = "entryId",
            entityColumn = "eventId"
    )
    public List<TimeEntry> timeEntryList;
}

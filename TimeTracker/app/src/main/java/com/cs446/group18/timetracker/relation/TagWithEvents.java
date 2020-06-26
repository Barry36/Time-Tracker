package com.cs446.group18.timetracker.relation;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.cs446.group18.timetracker.entity.Event;
import com.cs446.group18.timetracker.entity.EventTagCrossRef;
import com.cs446.group18.timetracker.entity.Tag;

import java.util.List;

public class TagWithEvents {
    @Embedded
    private Tag tag;

    @Relation(
            parentColumn = "tag_id",
            entityColumn = "event_id",
            associateBy = @Junction(EventTagCrossRef.class)
    )
    private List<Event> events;

    public Tag getTag() {
        return tag;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}

package com.cs446.group18.timetracker.relation;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import com.cs446.group18.timetracker.entity.Event;
import com.cs446.group18.timetracker.entity.EventTagCrossRef;
import com.cs446.group18.timetracker.entity.Tag;

import java.util.ArrayList;
import java.util.List;

public class EventWithTags {
    @Embedded
    private Event event;

    @Relation(
            parentColumn = "event_id",
            entityColumn = "tag_id",
            associateBy = @Junction(EventTagCrossRef.class)
    )
    private List<Tag> tags = new ArrayList<>();

    public Event getEvent() {
        return event;
    }

    public List<Tag> getTags() {
        return tags;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setTags(List<Tag> tags) {
        this.tags = tags;
    }
}

package com.cs446.group18.timetracker.relation;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.cs446.group18.timetracker.entity.Event;
import com.cs446.group18.timetracker.entity.Goal;

public class EventAndGoal {
    @Embedded
    private Event event;

    @Relation(
            parentColumn = "event_id",
            entityColumn = "goal_id"
    )

    private Goal goal;

    public Event getEvent() {
        return event;
    }

    public Goal getGoal() {
        return goal;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setGoal(Goal goal) {
        this.goal = goal;
    }
}

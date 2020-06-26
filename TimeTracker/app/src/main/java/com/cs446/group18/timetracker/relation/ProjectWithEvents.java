package com.cs446.group18.timetracker.relation;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.cs446.group18.timetracker.entity.Event;
import com.cs446.group18.timetracker.entity.Project;

import java.util.ArrayList;
import java.util.List;

public class ProjectWithEvents {
    @Embedded
    private Project project;

    @Relation(parentColumn = "project_id", entityColumn = "event_id")
    private List<Event> events = new ArrayList<>();

    public Project getProject() {
        return project;
    }

    public List<Event> getEvents() {
        return events;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public void setEvents(List<Event> events) {
        this.events = events;
    }
}

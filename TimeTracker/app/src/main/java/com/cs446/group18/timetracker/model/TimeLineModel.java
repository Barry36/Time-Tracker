package com.cs446.group18.timetracker.model;

import com.cs446.group18.timetracker.entity.Event;

public class TimeLineModel {
    private String eventName;
    private String time;
    private Event event;

    public TimeLineModel(String eventName, String time, Event event) {
        this.eventName = eventName;
        this.time = time;
        this.event = event;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public Event getEvent() { return event; }

    public void setEvent(Event event) {this.event = event; }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}

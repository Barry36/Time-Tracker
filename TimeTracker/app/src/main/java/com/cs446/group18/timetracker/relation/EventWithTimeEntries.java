package com.cs446.group18.timetracker.relation;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.cs446.group18.timetracker.entity.Event;
import com.cs446.group18.timetracker.entity.TimeEntry;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EventWithTimeEntries {
    @Embedded
    private Event event;

    @Relation(parentColumn = "event_id", entityColumn = "event_id")
    private List<TimeEntry> timeEntries = new ArrayList<>();

    public Event getEvent() {
        return event;
    }

    public List<TimeEntry> getTimeEntries() {
        return timeEntries;
    }

    public List<TimeEntry> getCurrWeekTimeEntries() {
        List<TimeEntry> currMonthTimeEntries = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        int CURR_WEEK = c.get(Calendar.WEEK_OF_MONTH);
        for (int i = 0; i < timeEntries.size(); i++) {
            TimeEntry e = timeEntries.get(i);
            c.setTime(e.getStartTime());
            if (c.get(Calendar.WEEK_OF_MONTH) == CURR_WEEK) {
                currMonthTimeEntries.add(e);
            }
        }
        return currMonthTimeEntries;
    }

    public List<TimeEntry> getCurrMonthTimeEntries() {
        List<TimeEntry> currMonthTimeEntries = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        int CURR_MONTH = c.get(Calendar.MONTH);
        for (int i = 0; i < timeEntries.size(); i++) {
            TimeEntry e = timeEntries.get(i);
            c.setTime(e.getStartTime());
            if (c.get(Calendar.MONTH) == CURR_MONTH) {
                currMonthTimeEntries.add(e);
            }
        }
        return currMonthTimeEntries;
    }

    public List<TimeEntry> getCurrYearTimeEntries() {
        List<TimeEntry> currYearTimeEntries = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        int CURR_YEAR = c.get(Calendar.YEAR);
        for (int i = 0; i < timeEntries.size(); i++) {
            TimeEntry e = timeEntries.get(i);
            c.setTime(e.getStartTime());
            if (c.get(Calendar.YEAR) == CURR_YEAR) {
                currYearTimeEntries.add(e);
            }
        }
        return currYearTimeEntries;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setTimeEntries(List<TimeEntry> timeEntries) {
        this.timeEntries = timeEntries;
    }
}

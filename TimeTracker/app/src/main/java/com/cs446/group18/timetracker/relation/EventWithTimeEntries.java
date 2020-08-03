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

    public List<TimeEntry> getSelectedTimeEntries(boolean askThisYear, boolean askThisMonth,
                                                  boolean askThisWeek) {
        List<TimeEntry> selectedTimeEntries = new ArrayList<>();
        Calendar c = Calendar.getInstance();
        int CURR_YEAR = c.get(Calendar.YEAR);
        int CURR_MONTH = c.get(Calendar.MONTH);
        int CURR_WEEK = c.get(Calendar.WEEK_OF_MONTH);

        for (int i = 0; i < timeEntries.size(); i++) {
            TimeEntry e = timeEntries.get(i);
            c.setTime(e.getStartTime());
            if (!askThisYear || c.get(Calendar.YEAR) == CURR_YEAR) {
                if (!askThisMonth || (c.get(Calendar.MONTH) == CURR_MONTH)) {
                    if (!askThisWeek || c.get(Calendar.WEEK_OF_MONTH) == CURR_WEEK) {
                        selectedTimeEntries.add(e);
                    }
                }
            }
        }
        return selectedTimeEntries;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public void setTimeEntries(List<TimeEntry> timeEntries) {
        this.timeEntries = timeEntries;
    }
}

package com.cs446.group18.timetracker.ui;

import com.cs446.group18.timetracker.entity.Geolocation;
import com.cs446.group18.timetracker.entity.TimeEntry;

public interface FragmentDatabaseSaver {
    public void updateGeoLocation(Geolocation geolocation);
    public long updateTimeEntry(TimeEntry geolocation);
}

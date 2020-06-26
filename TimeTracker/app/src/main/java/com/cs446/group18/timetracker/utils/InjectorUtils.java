package com.cs446.group18.timetracker.utils;

import android.content.Context;

import com.cs446.group18.timetracker.persistence.TimeTrackerDatabase;
import com.cs446.group18.timetracker.repository.EventRepository;
import com.cs446.group18.timetracker.repository.TimeEntryRepository;
import com.cs446.group18.timetracker.vm.EventListViewModelFactory;
import com.cs446.group18.timetracker.vm.TimeEntryListViewModelFactory;

public class InjectorUtils {
    private static TimeEntryRepository getTimeEntryRepository(Context context) {
        return TimeEntryRepository.getInstance(TimeTrackerDatabase.getInstance(context).timeEntryDao());
    }

    private static EventRepository getEventRepository(Context context) {
        return EventRepository.getInstance(TimeTrackerDatabase.getInstance(context).eventDao());
    }

    public static TimeEntryListViewModelFactory provideTimeEntryListViewModelFactory(Context context) {
        return new TimeEntryListViewModelFactory(getTimeEntryRepository(context));
    }

    public static EventListViewModelFactory provideEventListViewModelFactory(Context context) {
        return new EventListViewModelFactory(getEventRepository(context));
    }
}

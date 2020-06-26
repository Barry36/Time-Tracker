package com.cs446.group18.timetracker.utils;

import android.content.Context;

import com.cs446.group18.timetracker.persistence.TimeTrackerDatabase;
import com.cs446.group18.timetracker.repository.TimeEntryRepository;
import com.cs446.group18.timetracker.vm.TimeEntryListViewModelFactory;

public class InjectorUtil {
    private static TimeEntryRepository getTimeEntryRepository(Context context) {
        return TimeEntryRepository.getInstance(TimeTrackerDatabase.getInstance(context).timeEntryDao());
    }

    public static TimeEntryListViewModelFactory provideTimeEntryListViewModelFactory(Context context) {
        return new TimeEntryListViewModelFactory(getTimeEntryRepository(context));
    }
}

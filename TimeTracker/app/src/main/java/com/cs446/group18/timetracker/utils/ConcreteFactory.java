package com.cs446.group18.timetracker.utils;

import android.content.Context;

import com.cs446.group18.timetracker.persistence.TimeTrackerDatabase;
import com.cs446.group18.timetracker.repository.EventRepository;
import com.cs446.group18.timetracker.repository.GeolocationRepository;
import com.cs446.group18.timetracker.repository.TimeEntryRepository;
import com.cs446.group18.timetracker.vm.EventListViewModelFactory;
import com.cs446.group18.timetracker.vm.GeolocationViewModelFactory;
import com.cs446.group18.timetracker.vm.TimeEntryListViewModelFactory;

public class ConcreteFactory implements AbstractFactory {
    private static TimeEntryRepository getTimeEntryRepository(Context context) {
        return TimeEntryRepository.getInstance(TimeTrackerDatabase.getInstance(context).timeEntryDao());
    }

    private static EventRepository getEventRepository(Context context) {
        return EventRepository.getInstance(TimeTrackerDatabase.getInstance(context).eventDao());
    }

    private static GeolocationRepository getGeolocationRepository(Context context) {
        return GeolocationRepository.getInstance(TimeTrackerDatabase.getInstance(context).geolocationDao());
    }

    @Override
    public TimeEntryListViewModelFactory provideTimeEntryListViewModelFactory(Context context) {
        return new TimeEntryListViewModelFactory(getTimeEntryRepository(context));
    }

    @Override
    public EventListViewModelFactory provideEventListViewModelFactory(Context context) {
        return new EventListViewModelFactory(getEventRepository(context));
    }

    @Override
    public GeolocationViewModelFactory provideGeolocationViewModelFactory(Context context) {
        return new GeolocationViewModelFactory(getGeolocationRepository(context));
    }
}

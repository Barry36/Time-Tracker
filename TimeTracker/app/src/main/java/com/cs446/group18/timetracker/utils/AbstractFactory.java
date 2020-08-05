package com.cs446.group18.timetracker.utils;

import android.content.Context;

import com.cs446.group18.timetracker.vm.EventListViewModelFactory;
import com.cs446.group18.timetracker.vm.GeolocationViewModelFactory;
import com.cs446.group18.timetracker.vm.TimeEntryListViewModelFactory;

// abstract factory responsible for creating families of related or dependent objects without specifying their concrete classes
public interface AbstractFactory {
    TimeEntryListViewModelFactory provideTimeEntryListViewModelFactory(Context context);
    EventListViewModelFactory provideEventListViewModelFactory(Context context);
    GeolocationViewModelFactory provideGeolocationViewModelFactory(Context context);
}

package com.cs446.group18.timetracker.vm;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.cs446.group18.timetracker.repository.TimeEntryRepository;

public class TimeEntryListViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private TimeEntryRepository timeEntryRepository;

    public TimeEntryListViewModelFactory(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        TimeEntryViewModel timeEntryViewModel = new TimeEntryViewModel(timeEntryRepository);
        return (T) timeEntryViewModel;
    }
}

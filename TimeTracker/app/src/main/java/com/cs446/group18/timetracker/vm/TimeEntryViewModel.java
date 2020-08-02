package com.cs446.group18.timetracker.vm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cs446.group18.timetracker.entity.TimeEntry;
import com.cs446.group18.timetracker.repository.TimeEntryRepository;

import java.util.List;

public class TimeEntryViewModel extends ViewModel {
    private TimeEntryRepository repository;

    // ViewModel can survive after the activity is destroyed
    // Repository needs a context to instantiate the database,
    // but if we reference activity context, it will cause memory leak
    public TimeEntryViewModel(TimeEntryRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<TimeEntry>> getTimeEntries() {
        return repository.getTimeEntries();
    }

//    public LiveData<List<TimeEntry>> getTimeEntriesByEventID(int event_id){
//        return repository.getTimeEntriesByEventID(event_id);
//    }

    public LiveData<List<TimeEntry>> getTimeEntriesByEventID(long event_id) {
        return repository.getTimeEntriesByEventID(event_id);
    }

    public void insert(TimeEntry timeEntry) {
        repository.createTimeEntry(timeEntry);
    }

    public void update(TimeEntry timeEntry) {
        repository.updateTimeEntry(timeEntry);
    }

    public void delete(TimeEntry timeEntry) {
        repository.deleteTimeEntry(timeEntry);
    }
}

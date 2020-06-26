package com.cs446.group18.timetracker.vm;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.cs446.group18.timetracker.entity.TimeEntry;
import com.cs446.group18.timetracker.repository.TimeEntryRepository;

import java.util.List;

public class TimeEntryViewModel extends AndroidViewModel {
    private TimeEntryRepository repository;
    private LiveData<List<TimeEntry>> timeEntries;

    // ViewModel can survive after the activity is destroyed
    // Repository needs a context to instantiate the database,
    // but if we reference activity context, it will cause memory leak
    public TimeEntryViewModel(@NonNull Application application) {
        super(application);
        repository = new TimeEntryRepository(application);
        timeEntries = repository.getTimeEntries();
    }

    public void insert(TimeEntry timeEntry) {
        repository.insert(timeEntry);
    }
    public void update(TimeEntry timeEntry) {
        repository.update(timeEntry);
    }
    public void delete(TimeEntry timeEntry) {
        repository.delete(timeEntry);
    }
    public LiveData<List<TimeEntry>> getTimeEntries() {
        return timeEntries;
    }
}

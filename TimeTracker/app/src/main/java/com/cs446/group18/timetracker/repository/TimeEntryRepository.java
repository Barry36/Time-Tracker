package com.cs446.group18.timetracker.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.cs446.group18.timetracker.dao.TimeEntryDAO;
import com.cs446.group18.timetracker.entity.TimeEntry;
import com.cs446.group18.timetracker.persistence.TimeTrackerDatabase;

import java.util.List;

public class TimeEntryRepository {
    private TimeEntryDAO timeEntryDAO;
    private LiveData<List<TimeEntry>> timeEntries;

    public TimeEntryRepository(Application application) {
        TimeTrackerDatabase timeTrackerDatabase = TimeTrackerDatabase.getInstance(application);
        timeEntryDAO = timeTrackerDatabase.timeEntryDAO();
        timeEntries = timeEntryDAO.getAll();
    }

    // Room does not allow database operation on the main thread, it will freeze the app
    // Hence we need async task to execute operation
    public void insert(TimeEntry timeEntry) {
        new InsertTimeEntryAsyncTask(timeEntryDAO).execute(timeEntry);
    }

    public void update(TimeEntry timeEntry) {
        new UpdateTimeEntryAsyncTask(timeEntryDAO).execute(timeEntry);
    }

    public void delete(TimeEntry timeEntry) {
        new DeleteTimeEntryAsyncTask(timeEntryDAO).execute(timeEntry);
    }

    public LiveData<List<TimeEntry>> getTimeEntries() {
        return timeEntries;
    }

    // static class does not have reference to repository itself, otherwise will cause memory leak
    private static class InsertTimeEntryAsyncTask extends AsyncTask<TimeEntry, Void, Void> {
        private TimeEntryDAO timeEntryDAO;

        private InsertTimeEntryAsyncTask(TimeEntryDAO timeEntryDAO) {
            this.timeEntryDAO = timeEntryDAO;
        }

        @Override
        protected Void doInBackground(TimeEntry... timeEntries) {
            timeEntryDAO.insert(timeEntries[0]);
            return null;
        }
    }

    private static class UpdateTimeEntryAsyncTask extends AsyncTask<TimeEntry, Void, Void> {
        private TimeEntryDAO timeEntryDAO;

        private UpdateTimeEntryAsyncTask(TimeEntryDAO timeEntryDAO) {
            this.timeEntryDAO = timeEntryDAO;
        }

        @Override
        protected Void doInBackground(TimeEntry... timeEntries) {
            timeEntryDAO.update(timeEntries[0]);
            return null;
        }
    }

    private static class DeleteTimeEntryAsyncTask extends AsyncTask<TimeEntry, Void, Void> {
        private TimeEntryDAO timeEntryDAO;

        private DeleteTimeEntryAsyncTask(TimeEntryDAO timeEntryDAO) {
            this.timeEntryDAO = timeEntryDAO;
        }

        @Override
        protected Void doInBackground(TimeEntry... timeEntries) {
            timeEntryDAO.delete(timeEntries[0]);
            return null;
        }
    }

}

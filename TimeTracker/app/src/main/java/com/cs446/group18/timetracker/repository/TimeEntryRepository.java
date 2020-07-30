package com.cs446.group18.timetracker.repository;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.cs446.group18.timetracker.dao.TimeEntryDao;
import com.cs446.group18.timetracker.entity.TimeEntry;
import com.cs446.group18.timetracker.relation.EventWithTimeEntries;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class TimeEntryRepository {
    private TimeEntryDao timeEntryDao;
    private static volatile TimeEntryRepository instance = null;

    public TimeEntryRepository(TimeEntryDao timeEntryDao) {
        this.timeEntryDao = timeEntryDao;
    }

    public static TimeEntryRepository getInstance(TimeEntryDao timeEntryDao) {
        if (instance == null) {
            synchronized (TimeEntryRepository.class) {
                if (instance == null)
                    instance = new TimeEntryRepository(timeEntryDao);
            }
        }
        return instance;
    }

    public LiveData<List<TimeEntry>> getTimeEntries() {
        return timeEntryDao.getAllTimeEntries();
    }

    public TimeEntry getTimeEntryById(long id) {
        return timeEntryDao.getTimeEntryById(id);
    }


//    public LiveData<List<TimeEntry>> getTimeEntriesByEventID(int event_id){
//        return timeEntryDao.getTimeEntriesByEventID(event_id);
//    }

    public LiveData<List<TimeEntry>> getTimeEntriesByEventID(long event_id){
        return timeEntryDao.getTimeEntriesByEventID(event_id);
    }



    public LiveData<List<EventWithTimeEntries>> getEventWithTimeEntries() {
        return timeEntryDao.getEventWithTimeEntries();
    }

    // Room does not allow database operation on the main thread, it will freeze the app
    // Hence we need async task to execute operation
    public long createTimeEntry(TimeEntry timeEntry) {
        try {
            return new InsertTimeEntryAsyncTask(timeEntryDao).execute(timeEntry).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static class InsertTimeEntryAsyncTask extends AsyncTask<TimeEntry, Void, Long> {
        private TimeEntryDao timeEntryDao;
        private InsertTimeEntryAsyncTask(TimeEntryDao timeEntryDao) {
            this.timeEntryDao = timeEntryDao;
        }

        @Override
        protected Long doInBackground(TimeEntry... timeEntries) {
            return timeEntryDao.insert(timeEntries[0]);
        }

    }

    public void updateTimeEntry(TimeEntry timeEntry) {
        AsyncTask.execute(() -> timeEntryDao.update(timeEntry));
    }

    public void deleteTimeEntry(TimeEntry timeEntry) {
        AsyncTask.execute(() -> timeEntryDao.delete(timeEntry));
    }
}

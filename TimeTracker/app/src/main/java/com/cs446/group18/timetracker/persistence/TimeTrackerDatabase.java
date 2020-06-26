package com.cs446.group18.timetracker.persistence;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cs446.group18.timetracker.dao.TimeEntryDAO;
import com.cs446.group18.timetracker.entity.TimeEntry;
import com.cs446.group18.timetracker.util.DateTimeConverter;

import java.text.SimpleDateFormat;
import java.util.Date;

@Database(entities = {TimeEntry.class}, version = 1, exportSchema = false)
@TypeConverters({DateTimeConverter.class})
public abstract class TimeTrackerDatabase extends RoomDatabase {
    // create a singleton instance of database
    private static TimeTrackerDatabase instance;

    public abstract TimeEntryDAO timeEntryDAO();

    // synchronized is used to avoid concurrent access in multithreading environment
    public static synchronized TimeTrackerDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    TimeTrackerDatabase.class, "tracker_database.db")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    // populate sample data when application launches
    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDBAsyncTask(instance).execute();
        }
    };

    private static class PopulateDBAsyncTask extends AsyncTask<Void, Void, Void> {
        private TimeEntryDAO timeEntryDAO;

        private PopulateDBAsyncTask(TimeTrackerDatabase db) {
            this.timeEntryDAO = db.timeEntryDAO();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            timeEntryDAO.insert(new TimeEntry(DateTimeConverter.fromTimestamp("2020-07-22 18:26:00"), DateTimeConverter.fromTimestamp("2020-07-22 18:40:00"), 1200L));
            timeEntryDAO.insert(new TimeEntry(DateTimeConverter.fromTimestamp("2020-07-23 10:15:00"), DateTimeConverter.fromTimestamp("2020-07-23 10:20:00"), 300L));
            return null;
        }
    }
}

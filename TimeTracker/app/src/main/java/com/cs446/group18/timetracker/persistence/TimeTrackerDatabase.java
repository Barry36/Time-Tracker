package com.cs446.group18.timetracker.persistence;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cs446.group18.timetracker.dao.EventDao;
import com.cs446.group18.timetracker.dao.GoalDao;
import com.cs446.group18.timetracker.dao.ProjectDao;
import com.cs446.group18.timetracker.dao.TimeEntryDao;
import com.cs446.group18.timetracker.entity.Event;
import com.cs446.group18.timetracker.entity.Goal;
import com.cs446.group18.timetracker.entity.Project;
import com.cs446.group18.timetracker.entity.Tag;
import com.cs446.group18.timetracker.entity.TimeEntry;
import com.cs446.group18.timetracker.utils.DateTimeConverter;

// This file is equal to JDBC, for thread safefy, using Singleton Pattern
// Add new entities, need change this line below

@Database(entities = {TimeEntry.class, Event.class, Tag.class, Project.class, Goal.class}, version = 1, exportSchema = false)
@TypeConverters({DateTimeConverter.class})
public abstract class TimeTrackerDatabase extends RoomDatabase {
    // create a singleton instance of database
    private static TimeTrackerDatabase instance;

    public abstract TimeEntryDao timeEntryDao();
    public abstract EventDao eventDao();
    public abstract ProjectDao projectDao();
    public abstract GoalDao goalDao();

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
        private TimeEntryDao timeEntryDao;
        private EventDao eventDao;
        private ProjectDao projectDao;
        private GoalDao goalDao;

        private PopulateDBAsyncTask(TimeTrackerDatabase db) {
            this.timeEntryDao = db.timeEntryDao();
            this.eventDao = db.eventDao();
            this.projectDao = db.projectDao();
            this.goalDao = db.goalDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            projectDao.insert(new Project("Study", "N/A", "#FF4081"));
            projectDao.insert(new Project("Rest", "N/A", "#3F51B5"));
            eventDao.insert(new Event(1,"Prepare for interview", "LC 161 & LC 162"));
            eventDao.insert(new Event(2, "Watch Youtube", "Watch drama"));
            timeEntryDao.insert(new TimeEntry(1, DateTimeConverter.fromTimestamp("2020-07-22 18:20:00"), DateTimeConverter.fromTimestamp("2020-07-22 18:40:00"), 1200L));
            timeEntryDao.insert(new TimeEntry(1, DateTimeConverter.fromTimestamp("2020-07-23 10:15:00"), DateTimeConverter.fromTimestamp("2020-07-23 10:20:00"), 300L));
            timeEntryDao.insert(new TimeEntry(2, DateTimeConverter.fromTimestamp("2020-07-22 11:15:00"), DateTimeConverter.fromTimestamp("2020-07-22 11:40:00"), 1500L));
            goalDao.insert(new Goal(1, "Study Goal", "N/A", 20));
            goalDao.insert(new Goal(1, "Rest Goal", "N/A", 20));
            return null;
        }
    }
}

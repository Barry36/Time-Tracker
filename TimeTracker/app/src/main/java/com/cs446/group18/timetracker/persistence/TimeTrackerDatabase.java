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

@Database(entities = {TimeEntry.class, Event.class, Tag.class, Project.class, Goal.class}, version = 1, exportSchema = false)
@TypeConverters({DateTimeConverter.class})
public abstract class TimeTrackerDatabase extends RoomDatabase {
    // create a singleton instance of database
    private static volatile TimeTrackerDatabase instance = null;

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
            eventDao.insert(new Event(2, "Exercise", "Go to the gym"));
            eventDao.insert(new Event(2, "Lunch", "have lunch"));
            eventDao.insert(new Event(2, "Study", "study for midterm"));
            
            timeEntryDao.insert(new TimeEntry(1, DateTimeConverter.fromTimestamp("2020-07-15 18:20:00"), DateTimeConverter.fromTimestamp("2020-07-15 18:40:00"), 1200L));
            timeEntryDao.insert(new TimeEntry(1, DateTimeConverter.fromTimestamp("2020-07-13 10:15:00"), DateTimeConverter.fromTimestamp("2020-07-13 10:20:00"), 300L));
            timeEntryDao.insert(new TimeEntry(2, DateTimeConverter.fromTimestamp("2020-07-12 11:15:00"), DateTimeConverter.fromTimestamp("2020-07-12 11:40:00"), 1500L));
            timeEntryDao.insert(new TimeEntry(3, DateTimeConverter.fromTimestamp("2020-07-13 11:15:00"), DateTimeConverter.fromTimestamp("2020-07-13 11:40:00"), 1500L));
            timeEntryDao.insert(new TimeEntry(4, DateTimeConverter.fromTimestamp("2020-07-10 11:15:00"), DateTimeConverter.fromTimestamp("2020-07-10 11:40:00"), 1500L));
            timeEntryDao.insert(new TimeEntry(5, DateTimeConverter.fromTimestamp("2020-07-11 11:15:00"), DateTimeConverter.fromTimestamp("2020-07-11 11:40:00"), 1500L));
            timeEntryDao.insert(new TimeEntry(5, DateTimeConverter.fromTimestamp("2020-07-11 16:15:00"), DateTimeConverter.fromTimestamp("2020-07-11 16:40:00"), 1500L));

            goalDao.insert(new Goal(1, "Study Goal", "I have to study for my final exam", 20, 100));
            goalDao.insert(new Goal(2, "Rest Goal", "I need some rest", 80, 100));
            return null;
        }
    }
}

package com.cs446.group18.timetracker.persistence;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cs446.group18.timetracker.R;
import com.cs446.group18.timetracker.dao.EventDao;
import com.cs446.group18.timetracker.dao.GeolocationDao;
import com.cs446.group18.timetracker.dao.GoalDao;
import com.cs446.group18.timetracker.dao.TimeEntryDao;
import com.cs446.group18.timetracker.entity.Event;
import com.cs446.group18.timetracker.entity.Geolocation;
import com.cs446.group18.timetracker.entity.Goal;
import com.cs446.group18.timetracker.entity.Tag;
import com.cs446.group18.timetracker.entity.TimeEntry;
import com.cs446.group18.timetracker.utils.DateTimeConverter;

@Database(entities = {TimeEntry.class, Event.class, Tag.class, Goal.class, Geolocation.class}, version = 1, exportSchema = false)
@TypeConverters({DateTimeConverter.class})
public abstract class TimeTrackerDatabase extends RoomDatabase {
    // create a singleton instance of database
    private static volatile TimeTrackerDatabase instance = null;

    public abstract TimeEntryDao timeEntryDao();

    public abstract EventDao eventDao();

    public abstract GoalDao goalDao();

    public abstract GeolocationDao geolocationDao();

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
        private GoalDao goalDao;
        private GeolocationDao geolocationDao;

        private PopulateDBAsyncTask(TimeTrackerDatabase db) {
            this.timeEntryDao = db.timeEntryDao();
            this.eventDao = db.eventDao();
            this.goalDao = db.goalDao();
            this.geolocationDao = db.geolocationDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            eventDao.insert(new Event("Study", "Prepare for midterm", R.drawable.ic_homework));
            eventDao.insert(new Event("Rest", "Have some sleep", R.drawable.ic_yoga));
            eventDao.insert(new Event("Exercise", "Go to the gym", R.drawable.ic_soccer));
            eventDao.insert(new Event("Meal", "Have meal", R.drawable.ic_cooking));
            eventDao.insert(new Event("Selfcare", "Dental", R.drawable.ic_hospital));
            eventDao.insert(new Event("Chores", "Flute", R.drawable.ic_music));
            eventDao.insert(new Event("School", "Takes lectures", R.drawable.ic_task));
            eventDao.insert(new Event("Groceries", "Fruits and vegetables", R.drawable.ic_shopping_cart));
            eventDao.insert(new Event("Travel", "Go to banff", R.drawable.ic_compass));
            eventDao.insert(new Event("Entertainment", "TV shows, Movie", R.drawable.ic_television));
            eventDao.insert(new Event("Appointment", "Virtual meeting", R.drawable.ic_email));
            eventDao.insert(new Event("Shopping", "Clothes and shoes", R.drawable.ic_shopping_cart));

            timeEntryDao.insert(new TimeEntry(4, DateTimeConverter.fromTimestamp("2019-07-20 20:15:00"),
                    DateTimeConverter.fromTimestamp("2019-07-20 21:40:00"),
                    DateTimeConverter.fromTimestamp("2019-07-20 21:40:00").getTime()
                            - DateTimeConverter.fromTimestamp("2019-07-20 20:15:00").getTime()));
            geolocationDao.insert(new Geolocation(1, 43.4203, -80.4389)); // Cinplex

            timeEntryDao.insert(new TimeEntry(1, DateTimeConverter.fromTimestamp("2020-01-18 9:15:00"),
                    DateTimeConverter.fromTimestamp("2020-01-18 11:40:00"),
                    DateTimeConverter.fromTimestamp("2020-01-18 11:40:00").getTime()
                            - DateTimeConverter.fromTimestamp("2020-01-18 9:15:00").getTime()));
            geolocationDao.insert(new Geolocation(2, 43.4736, -80.5370)); // Blair House

            timeEntryDao.insert(new TimeEntry(1, DateTimeConverter.fromTimestamp("2020-03-22 14:15:00"),
                    DateTimeConverter.fromTimestamp("2020-03-22 16:40:00"),
                    DateTimeConverter.fromTimestamp("2020-03-22 16:40:00").getTime()
                            - DateTimeConverter.fromTimestamp("2020-03-22 14:15:00").getTime()));
            geolocationDao.insert(new Geolocation(3, 43.4736, -80.5370)); // Blair House

            timeEntryDao.insert(new TimeEntry(4, DateTimeConverter.fromTimestamp("2020-04-10 20:15:00"),
                    DateTimeConverter.fromTimestamp("2020-04-10 20:40:00"),
                    DateTimeConverter.fromTimestamp("2020-04-10 20:40:00").getTime()
                            - DateTimeConverter.fromTimestamp("2020-04-10 20:15:00").getTime()));
            geolocationDao.insert(new Geolocation(1, 43.4818, -80.5256)); // McDonald's

            timeEntryDao.insert(new TimeEntry(4, DateTimeConverter.fromTimestamp("2020-05-01 17:20:00"),
                    DateTimeConverter.fromTimestamp("2020-05-01 18:40:00"),
                    Math.abs(DateTimeConverter.fromTimestamp("2020-05-01 18:40:00").getTime()
                            - DateTimeConverter.fromTimestamp("2020-05-01 17:20:00").getTime())));
            geolocationDao.insert(new Geolocation(5, 43.4653, -80.5226)); // Restaurant

            timeEntryDao.insert(new TimeEntry(1, DateTimeConverter.fromTimestamp("2020-06-20 8:10:00"),
                    DateTimeConverter.fromTimestamp("2020-06-20 10:40:00"),
                    Math.abs(DateTimeConverter.fromTimestamp("2020-06-20 10:40:00").getTime()
                            - DateTimeConverter.fromTimestamp("2020-06-20 8:10:00").getTime())));
            geolocationDao.insert(new Geolocation(6, 43.4736, -80.5370)); // Blair House

            timeEntryDao.insert(new TimeEntry(3, DateTimeConverter.fromTimestamp("2020-07-02 14:15:00"),
                    DateTimeConverter.fromTimestamp("2020-07-02 16:20:00"),
                    DateTimeConverter.fromTimestamp("2020-07-02 16:20:00").getTime()
                            - DateTimeConverter.fromTimestamp("2020-07-02 14:15:00").getTime()));
            geolocationDao.insert(new Geolocation(7, 43.4043, -80.5021)); // Good Life

            timeEntryDao.insert(new TimeEntry(7, DateTimeConverter.fromTimestamp("2020-07-15 10:15:00"),
                    DateTimeConverter.fromTimestamp("2020-07-15 10:40:00"),
                    DateTimeConverter.fromTimestamp("2020-07-15 10:40:00").getTime()
                            - DateTimeConverter.fromTimestamp("2020-07-15 10:15:00").getTime()));
            geolocationDao.insert(new Geolocation(8, 43.4879, -80.5290)); // School

            timeEntryDao.insert(new TimeEntry(1, DateTimeConverter.fromTimestamp("2020-08-01 9:15:00"),
                    DateTimeConverter.fromTimestamp("2020-08-01 11:20:00"),
                    DateTimeConverter.fromTimestamp("2020-08-01 11:20:00").getTime()
                            - DateTimeConverter.fromTimestamp("2020-08-01 9:15:00").getTime()));
            geolocationDao.insert(new Geolocation(9, 43.4736, -80.5370)); // Blair House

            timeEntryDao.insert(new TimeEntry(2, DateTimeConverter.fromTimestamp("2020-08-02 12:30:00"),
                    DateTimeConverter.fromTimestamp("2020-08-02 13:05:00"),
                    DateTimeConverter.fromTimestamp("2020-08-02 13:05:00").getTime()
                            - DateTimeConverter.fromTimestamp("2020-08-02 12:30:00").getTime()));
            geolocationDao.insert(new Geolocation(10, 43.4736, -80.5370)); // Blair House

            timeEntryDao.insert(new TimeEntry(3, DateTimeConverter.fromTimestamp("2020-08-02 18:30:00"),
                    DateTimeConverter.fromTimestamp("2020-08-02 19:40:00"),
                    DateTimeConverter.fromTimestamp("2020-08-02 19:40:00").getTime()
                            - DateTimeConverter.fromTimestamp("2020-08-02 18:30:00").getTime()));
            geolocationDao.insert(new Geolocation(5, 43.4043, -80.5021)); // Good Life

            timeEntryDao.insert(new TimeEntry(4, DateTimeConverter.fromTimestamp("2020-08-03 12:30:00"),
                    DateTimeConverter.fromTimestamp("2020-08-03 13:20:00"),
                    DateTimeConverter.fromTimestamp("2020-08-03 13:20:00").getTime()
                            - DateTimeConverter.fromTimestamp("2020-08-03 12:30:00").getTime()));
            geolocationDao.insert(new Geolocation(12, 43.4818, -80.5256)); // McDonald's

            timeEntryDao.insert(new TimeEntry(12, DateTimeConverter.fromTimestamp("2020-08-03 14:30:00"),
                    DateTimeConverter.fromTimestamp("2020-08-03 17:15:00"),
                    DateTimeConverter.fromTimestamp("2020-08-03 17:15:00").getTime()
                            - DateTimeConverter.fromTimestamp("2020-08-03 14:30:00").getTime()));
            geolocationDao.insert(new Geolocation(13, 43.4977, -80.5269)); // Cmall

            timeEntryDao.insert(new TimeEntry(4, DateTimeConverter.fromTimestamp("2020-08-03 16:50:00"),
                    DateTimeConverter.fromTimestamp("2020-08-03 18:20:00"),
                    DateTimeConverter.fromTimestamp("2020-08-03 18:20:00").getTime()
                            - DateTimeConverter.fromTimestamp("2020-08-03 16:50:00").getTime()));
            geolocationDao.insert(new Geolocation(14, 43.4657, -80.5227)); // Nick & Nat's Uptown 21

            timeEntryDao.insert(new TimeEntry(1, DateTimeConverter.fromTimestamp("2020-08-04 7:30:00"),
                    DateTimeConverter.fromTimestamp("2020-08-04 9:45:00"),
                    DateTimeConverter.fromTimestamp("2020-08-04 9:45:00").getTime()
                            - DateTimeConverter.fromTimestamp("2020-08-04 7:30:00").getTime()));
            geolocationDao.insert(new Geolocation(15, 43.4736, -80.5370)); // Blair House

            timeEntryDao.insert(new TimeEntry(4, DateTimeConverter.fromTimestamp("2020-08-04 11:30:00"),
                    DateTimeConverter.fromTimestamp("2020-08-04 12:30:00"),
                    DateTimeConverter.fromTimestamp("2020-08-04 12:30:00").getTime()
                            - DateTimeConverter.fromTimestamp("2020-08-04 11:30:00").getTime()));
            geolocationDao.insert(new Geolocation(16, 43.4653, -80.5226)); // Restaurant

            timeEntryDao.insert(new TimeEntry(3, DateTimeConverter.fromTimestamp("2020-08-04 13:50:00"),
                    DateTimeConverter.fromTimestamp("2020-08-04 15:35:00"),
                    DateTimeConverter.fromTimestamp("2020-08-04 15:35:00").getTime()
                            - DateTimeConverter.fromTimestamp("2020-08-04 13:50:00").getTime()));
            geolocationDao.insert(new Geolocation(17, 43.4043, -80.5021)); // Good Life

            timeEntryDao.insert(new TimeEntry(4, DateTimeConverter.fromTimestamp("2020-08-04 17:30:00"),
                    DateTimeConverter.fromTimestamp("2020-08-04 18:40:00"),
                    DateTimeConverter.fromTimestamp("2020-08-04 18:40:00").getTime()
                            - DateTimeConverter.fromTimestamp("2020-08-04 17:30:00").getTime()));
            geolocationDao.insert(new Geolocation(18, 43.4655, -80.5227)); // Captain Boil

            return null;
        }
    }
}

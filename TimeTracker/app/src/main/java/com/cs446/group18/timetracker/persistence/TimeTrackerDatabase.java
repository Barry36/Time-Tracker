package com.cs446.group18.timetracker.persistence;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cs446.group18.timetracker.dao.EventDao;
import com.cs446.group18.timetracker.dao.GeolocationDao;
import com.cs446.group18.timetracker.dao.GoalDao;
import com.cs446.group18.timetracker.dao.TimeEntryDao;
import com.cs446.group18.timetracker.entity.Event;
import com.cs446.group18.timetracker.entity.Geolocation;
import com.cs446.group18.timetracker.entity.Goal;
import com.cs446.group18.timetracker.entity.Tag;
import com.cs446.group18.timetracker.entity.TimeEntry;
import com.cs446.group18.timetracker.ui.MainActivity;
import com.cs446.group18.timetracker.utils.DateTimeConverter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

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

        private String loadJSONFromAsset(String filename) {
            LoadJSON lj = new LoadJSON();
            Context context = lj.getContext();
            String json = null;
            try {
                InputStream is = context.getAssets().open(filename);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");
            } catch (IOException ex) {
                ex.printStackTrace();
                return null;
            }
            return json;
        }

        private void importEventsFromJSON() {
            try {
                JSONObject obj = new JSONObject(loadJSONFromAsset("events.json"));
                JSONArray m_jArray = obj.getJSONArray("events");
                for (int i = 0; i < m_jArray.length(); i++) {
                    JSONObject jo_inside = m_jArray.getJSONObject(i);
                    String eventName = jo_inside.getString("eventName");
                    String description = jo_inside.getString("description");
                    int icon = jo_inside.getInt("icon");

                    eventDao.insert(new Event(eventName, description, icon));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void importTimeEntriesFromJSON() {
            try {
                JSONObject obj = new JSONObject(loadJSONFromAsset("timeEntries.json"));
                JSONArray m_jArray = obj.getJSONArray("timeEntries");
                for (int i = 0; i < m_jArray.length(); i++) {
                    JSONObject jo_inside = m_jArray.getJSONObject(i);
                    long eventID = jo_inside.getLong("eventID");
                    Date startDate = DateTimeConverter.fromTimestamp(jo_inside.getString("startDate"));
                    Date endDate = DateTimeConverter.fromTimestamp(jo_inside.getString("endDate"));
                    long duration = jo_inside.getLong("duration");
                    timeEntryDao.insert(new TimeEntry(eventID, startDate, endDate, duration));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void importGoalsFromJSON() {
            try {
                JSONObject obj = new JSONObject(loadJSONFromAsset("goals.json"));
                JSONArray m_jArray = obj.getJSONArray("goals");
                for (int i = 0; i < m_jArray.length(); i++) {
                    JSONObject jo_inside = m_jArray.getJSONObject(i);
                    long eventID = jo_inside.getLong("eventID");
                    String name = jo_inside.getString("name");
                    String description = jo_inside.getString("description");
                    int progressValue = jo_inside.getInt("progressValue");
                    int targetValue = jo_inside.getInt("targetValue");
                    goalDao.insert(new Goal(eventID, name, description, progressValue, targetValue));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void importGeolocationsFromJSON() {
            try {
                JSONObject obj = new JSONObject(loadJSONFromAsset("geolocations.json"));
                JSONArray m_jArray = obj.getJSONArray("geolocations");
                for (int i = 0; i < m_jArray.length(); i++) {
                    JSONObject jo_inside = m_jArray.getJSONObject(i);
                    long timeEntryId = jo_inside.getLong("timeEntryId");
                    long latitude = jo_inside.getLong("latitude");
                    long longitude = jo_inside.getLong("longitude");
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... voids) {
            importEventsFromJSON();
            importTimeEntriesFromJSON();
            importGoalsFromJSON();
            importGeolocationsFromJSON();
//            eventDao.insert(new Event("Study", "LC 161 & LC 162"));
//            eventDao.insert(new Event("Rest", "Watch drama"));
//            eventDao.insert(new Event("Exercise", "Go to the gym"));
//            eventDao.insert(new Event("Meal", "have lunch"));
//            timeEntryDao.insert(new TimeEntry(1, DateTimeConverter.fromTimestamp("2020-07-22 18:20:00"), DateTimeConverter.fromTimestamp("2020-07-22 18:40:00"), 1200L));
//            timeEntryDao.insert(new TimeEntry(1, DateTimeConverter.fromTimestamp("2020-07-23 10:15:00"), DateTimeConverter.fromTimestamp("2020-07-23 10:20:00"), 300L));
//            timeEntryDao.insert(new TimeEntry(2, DateTimeConverter.fromTimestamp("2020-07-22 11:15:00"), DateTimeConverter.fromTimestamp("2020-07-22 11:40:00"), 1500L));
//            timeEntryDao.insert(new TimeEntry(2, DateTimeConverter.fromTimestamp("2020-07-18 11:15:00"), DateTimeConverter.fromTimestamp("2020-07-18 11:40:00"), 1500L));
//            timeEntryDao.insert(new TimeEntry(2, DateTimeConverter.fromTimestamp("2020-07-17 11:15:00"), DateTimeConverter.fromTimestamp("2020-07-17 11:40:00"), 1500L));
//            goalDao.insert(new Goal(1, "Study Goal", "I have to study for my final exam", 20, 100));
//            goalDao.insert(new Goal(2, "Rest Goal", "I need some rest", 80, 100));
//            geolocationDao.insert(new Geolocation(1, 43.46552, -80.5226817)); // Captain Boil
//            geolocationDao.insert(new Geolocation(2, 43.46567, -80.522683)); // Nick & Nat's Uptown 21
//            geolocationDao.insert(new Geolocation(3, 43.46552, -80.5226817)); // Captain Boil
//            geolocationDao.insert(new Geolocation(4, 43.48177, -80.5255692)); // McDonald's
//            geolocationDao.insert(new Geolocation(5, 43.47363, -80.5370301)); // Blair House

            return null;
        }
    }
}

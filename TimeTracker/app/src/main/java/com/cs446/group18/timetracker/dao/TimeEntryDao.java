package com.cs446.group18.timetracker.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;
import androidx.room.Update;

import com.cs446.group18.timetracker.entity.TimeEntry;
import com.cs446.group18.timetracker.relation.EventWithTimeEntries;

import java.util.List;

@Dao
public interface TimeEntryDao {
    @Insert
    long insert(TimeEntry entry);

    @Update
    void update(TimeEntry entry);

    @Delete
    void delete(TimeEntry entry);

    @Query("SELECT * FROM time_entry_table ORDER BY start_time DESC")
    LiveData<List<TimeEntry>> getAllTimeEntries();


    @Query("SELECT * FROM time_entry_table WHERE event_id = :event_id")
    List<TimeEntry> getTimeEntriesByEventIDStatic(long event_id);


    @Query("SELECT * FROM time_entry_table WHERE event_id = :event_id")
    LiveData<List<TimeEntry>> getTimeEntriesByEventID(long event_id);

    @Query("SELECT * FROM time_entry_table ORDER BY event_id DESC")
    List<TimeEntry> getAllTimeEntriesStatic();

    @Query("SELECT * FROM time_entry_table WHERE time_entry_id = :time_entry_id")
    TimeEntry getTimeEntryById(long time_entry_id);

    @Transaction
    @Query("SELECT * FROM event_table")
    List<EventWithTimeEntries> getEventsWithTimeEntriesStatic();
}

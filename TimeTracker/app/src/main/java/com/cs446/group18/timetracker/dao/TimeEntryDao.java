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
    void insert(TimeEntry entry);

    @Update
    void update(TimeEntry entry);

    @Delete
    void delete(TimeEntry entry);

    @Query("SELECT * FROM time_entry_table ORDER BY start_time DESC")
    LiveData<List<TimeEntry>> getAllTimeEntries();

<<<<<<< HEAD
//    @Query("SELECT * FROM time_entry_table WHERE event_id = :event_id")
//    LiveData<List<TimeEntry>> getTimeEntriesByEventID(long event_id);


    @Query("SELECT * FROM time_entry_table WHERE event_id = :event_id")
    List<TimeEntry> getTimeEntriesByEventID(long event_id);

    @Query("SELECT * FROM time_entry_table ORDER BY event_id DESC")
    List<TimeEntry> getAllTimeEntriesStatic();

=======
>>>>>>> parent of b696c17... create expandable cardViewHolder
    @Transaction
    @Query("SELECT * FROM event_table")
    LiveData<List<EventWithTimeEntries>> getEventWithTimeEntries();
}

package com.cs446.group18.timetracker.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.cs446.group18.timetracker.entity.TimeEntry;

import java.util.List;

@Dao
public interface TimeEntryDAO {
    @Insert
    void insert(TimeEntry entry);

    @Update
    void update(TimeEntry entry);

    @Delete
    void delete(TimeEntry entry);

    @Query("SELECT * FROM entry_table ORDER BY startTime DESC")
    LiveData<List<TimeEntry>> getAll();
}

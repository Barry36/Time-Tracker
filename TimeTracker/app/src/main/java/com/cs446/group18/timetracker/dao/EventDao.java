package com.cs446.group18.timetracker.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.cs446.group18.timetracker.entity.Event;

import java.util.List;

@Dao
public interface EventDao {
    @Query("SELECT * FROM event_table")
    LiveData<List<Event>> getEvents();

    @Insert
    void insert(Event event);
}

package com.cs446.group18.timetracker.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.cs446.group18.timetracker.entity.Goal;

import java.util.List;

@Dao
public interface GoalDao {
    @Query("SELECT * FROM goal_table")
    LiveData<List<Goal>> getGoals();

    @Insert
    void insert(Goal goal);
}

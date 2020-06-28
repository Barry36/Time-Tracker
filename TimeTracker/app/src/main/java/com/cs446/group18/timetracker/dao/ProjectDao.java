package com.cs446.group18.timetracker.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.cs446.group18.timetracker.entity.Project;

import java.util.List;

@Dao
public interface ProjectDao {
    @Query("SELECT * FROM project_table")
    LiveData<List<Project>> getProjects();

    @Insert
    void insert(Project project);
}

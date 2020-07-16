package com.cs446.group18.timetracker.repository;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;


import com.cs446.group18.timetracker.dao.GoalDao;
import com.cs446.group18.timetracker.entity.Goal;

import java.util.List;

public class GoalRepository {
    private GoalDao goalDao;
    private static volatile  GoalRepository instance;

    public GoalRepository(GoalDao goalDao) {
        this.goalDao = goalDao;
    }

    public static GoalRepository getInstance(GoalDao goalDao) {
        if (instance == null) {
            synchronized(GoalRepository.class) {
                if (instance == null)
                    instance = new GoalRepository(goalDao);
            }
        }
        return instance;
    }

    public LiveData<List<Goal>> getGoals() {
        return goalDao.getGoals();
    }

    public void createGoal(Goal goal) {
        AsyncTask.execute(() -> goalDao.insert(goal));
    }
}

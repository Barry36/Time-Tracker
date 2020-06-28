package com.cs446.group18.timetracker.vm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cs446.group18.timetracker.entity.Goal;
import com.cs446.group18.timetracker.repository.GoalRepository;

import java.util.List;

public class GoalViewModel extends ViewModel {
    private GoalRepository repository;

    // ViewModel can survive after the activity is destroyed
    // Repository needs a context to instantiate the database,
    // but if we reference activity context, it will cause memory leak
    public GoalViewModel(GoalRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<Goal>> getGoals() {
        return repository.getGoals();
    }

    public void insert(Goal goal) {
        repository.createGoal(goal);
    }
}

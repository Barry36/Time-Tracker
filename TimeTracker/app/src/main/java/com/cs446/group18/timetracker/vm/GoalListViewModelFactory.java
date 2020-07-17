package com.cs446.group18.timetracker.vm;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.cs446.group18.timetracker.repository.GoalRepository;

public class GoalListViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private GoalRepository goalRepository;

    public GoalListViewModelFactory(GoalRepository goalRepository) {
        this.goalRepository = goalRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        GoalViewModel goalViewModel = new GoalViewModel(goalRepository);
        return (T) goalViewModel;
    }
}

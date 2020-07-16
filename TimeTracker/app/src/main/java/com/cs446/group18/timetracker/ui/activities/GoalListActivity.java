package com.cs446.group18.timetracker.ui.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.cs446.group18.timetracker.R;
import com.cs446.group18.timetracker.adapter.GoalListAdapter;
import com.cs446.group18.timetracker.entity.Goal;
import com.cs446.group18.timetracker.utils.InjectorUtils;
import com.cs446.group18.timetracker.vm.GoalListViewModelFactory;
import com.cs446.group18.timetracker.vm.GoalViewModel;

import java.util.ArrayList;
import java.util.List;

public class GoalListActivity extends AppCompatActivity {
    private List<Goal> goals = new ArrayList<>();
    RecyclerView recyclerView;
    private TextView textViewEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_list);
        GoalListAdapter adapter = new GoalListAdapter(goals);

        textViewEmpty = findViewById(R.id.empty_goal_list);
        recyclerView = findViewById(R.id.goal_list);
        recyclerView.setAdapter(adapter);

        subscribeUI(adapter);
    }

    private void subscribeUI(GoalListAdapter adapter) {
        GoalListViewModelFactory factory = InjectorUtils.provideGoalListViewModelFactory(this);
        GoalViewModel viewModel = new ViewModelProvider(this, factory).get(GoalViewModel.class);
        viewModel.getGoals().observe(this, new Observer<List<Goal>>() {
            @Override
            public void onChanged(@Nullable List<Goal> goals) {
                if (goals != null && !goals.isEmpty()) {
                    recyclerView.setVisibility(View.VISIBLE);
                    textViewEmpty.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    textViewEmpty.setVisibility(View.VISIBLE);
                }
                adapter.setGoals(goals);
            }
        });
    }
}
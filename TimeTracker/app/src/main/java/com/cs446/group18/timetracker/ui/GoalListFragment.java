package com.cs446.group18.timetracker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.cs446.group18.timetracker.R;
import com.cs446.group18.timetracker.adapter.GoalListAdapter;
import com.cs446.group18.timetracker.entity.Goal;
import com.cs446.group18.timetracker.utils.InjectorUtils;
import com.cs446.group18.timetracker.vm.GoalListViewModelFactory;
import com.cs446.group18.timetracker.vm.GoalViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class GoalListFragment extends Fragment {
    private List<Goal> goals = new ArrayList<>();
    RecyclerView recyclerView;
    private TextView textViewEmpty;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_goal_list, container, false);
        GoalListAdapter adapter = new GoalListAdapter(goals);

        textViewEmpty = view.findViewById(R.id.empty_goal_list);
        recyclerView = view.findViewById(R.id.goal_list);
        recyclerView.setAdapter(adapter);
        FloatingActionButton buttonAddGoal = view.findViewById(R.id.button_add_goal);
        buttonAddGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                AddGoalFragment addGoalFragment = new AddGoalFragment();
//                FragmentManager manager = getChildFragmentManager();
//                manager.beginTransaction()
//                        .replace(R.id.goals, addGoalFragment)
//                        .commit();
                Toast.makeText(view.getContext(), "Add new goal", Toast.LENGTH_SHORT).show();
            }
        });

        subscribeUI(adapter);
        return view;
    }

    private void subscribeUI(GoalListAdapter adapter) {
        GoalListViewModelFactory factory = InjectorUtils.provideGoalListViewModelFactory(getActivity());
        GoalViewModel viewModel = new ViewModelProvider(this, factory).get(GoalViewModel.class);
        viewModel.getGoals().observe(getViewLifecycleOwner(), new Observer<List<Goal>>() {
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

package com.cs446.group18.timetracker.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
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
        View goalListView = inflater.inflate(R.layout.fragment_goal_list, container, false);
        GoalListAdapter adapter = new GoalListAdapter(goals);
        GoalListViewModelFactory factory = InjectorUtils.provideGoalListViewModelFactory(getActivity());
        GoalViewModel viewModel = new ViewModelProvider(this, factory).get(GoalViewModel.class);

        textViewEmpty = goalListView.findViewById(R.id.empty_goal_list);
        recyclerView = goalListView.findViewById(R.id.goal_list);
        recyclerView.setAdapter(adapter);

        FloatingActionButton buttonAddGoal = goalListView.findViewById(R.id.button_add_goal);
        buttonAddGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme);
                View promptView = inflater.inflate(R.layout.prompt_add_goal, container, false);

                final EditText goalNameText = promptView.findViewById(R.id.goal_name);
                final EditText goalDescriptionText = promptView.findViewById(R.id.goal_description);
                Spinner dropdown = promptView.findViewById(R.id.spinner1);
                String[] items = new String[]{"Rest", "Study", "Life"};
                ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, items);
                dropdown.setAdapter(adapter);
                builder.setView(promptView)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String goalName = goalNameText.getText().toString();
                                String goalDescription = goalDescriptionText.getText().toString();
                                try {
                                    viewModel.insert(new Goal(1, goalName, goalDescription, 20, 100));
                                    Toast.makeText(goalListView.getContext(), "Add new goal: " + goalName, Toast.LENGTH_SHORT).show();
                                } catch (Exception e) {
                                    dialog.dismiss();
                                    AlertDialog.Builder errorBuilder = new AlertDialog.Builder(getActivity());
                                    errorBuilder.setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {
                                                    dialog.dismiss();
                                                }
                                            });
                                    errorBuilder.setTitle("Error");
                                    errorBuilder.setMessage("Please enter a valid input");
                                    AlertDialog error = errorBuilder.create();
                                    error.show();
                                }
                            }
                        })
                        .setNegativeButton("Cancel",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.cancel();
                                    }
                                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        subscribeUI(adapter, viewModel);
        return goalListView;
    }

    private void subscribeUI(GoalListAdapter adapter, GoalViewModel viewModel) {
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

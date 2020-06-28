package com.cs446.group18.timetracker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.cs446.group18.timetracker.R;
import com.cs446.group18.timetracker.adapter.EventListAdapter;
import com.cs446.group18.timetracker.entity.Event;
import com.cs446.group18.timetracker.entity.Goal;
import com.cs446.group18.timetracker.utils.InjectorUtils;
import com.cs446.group18.timetracker.vm.EventListViewModelFactory;
import com.cs446.group18.timetracker.vm.EventViewModel;
import com.cs446.group18.timetracker.vm.GoalListViewModelFactory;
import com.cs446.group18.timetracker.vm.GoalViewModel;

import java.util.List;

public class AddGoalFragment extends Fragment {
    private EditText editTextName;
    private EditText editTextDescription;
    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_goal, container, false);

        editTextName = view.findViewById(R.id.edit_text_name);
        editTextDescription = view.findViewById(R.id.edit_text_description);
        return view;
    }

    private void saveGoal() {
        String name = editTextName.getText().toString();
        String description = editTextDescription.getText().toString();
        if (name.trim().isEmpty() || description.trim().isEmpty()) {
            Toast.makeText(view.getContext(), "Please insert a title and description", Toast.LENGTH_SHORT).show();
            return;
        }
        GoalListViewModelFactory factory = InjectorUtils.provideGoalListViewModelFactory(getActivity());
        GoalViewModel viewModel = new ViewModelProvider(this, factory).get(GoalViewModel.class);
        viewModel.insert(new Goal(1, name, description, 50, 100));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_add_goal, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save_goal:
                saveGoal();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}

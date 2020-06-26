package com.cs446.group18.timetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.cs446.group18.timetracker.adapter.TimeEntryAdapter;
import com.cs446.group18.timetracker.entity.TimeEntry;
import com.cs446.group18.timetracker.vm.TimeEntryViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TimeEntryViewModel timeEntryViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        final TimeEntryAdapter adapter = new TimeEntryAdapter();
        recyclerView.setAdapter(adapter);

        // ViewModelProvider will handle dependency injection for us, we shouldn't instantiate the ViewModel ourselves
        timeEntryViewModel = new ViewModelProvider(this).get(TimeEntryViewModel.class);
        timeEntryViewModel.getTimeEntries().observe(this, new Observer<List<TimeEntry>>() {
            @Override
            public void onChanged(List<TimeEntry> timeEntries) {
                adapter.setEntries(timeEntries);
            }
        });
    }
}
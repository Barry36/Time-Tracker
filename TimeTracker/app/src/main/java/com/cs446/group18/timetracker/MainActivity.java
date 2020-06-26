package com.cs446.group18.timetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.cs446.group18.timetracker.adapter.TimeEntryAdapter;
import com.cs446.group18.timetracker.entity.TimeEntry;
import com.cs446.group18.timetracker.utils.InjectorUtil;
import com.cs446.group18.timetracker.vm.TimeEntryListViewModelFactory;
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

        TimeEntryListViewModelFactory factory = InjectorUtil.provideTimeEntryListViewModelFactory(getApplicationContext());
        timeEntryViewModel = new ViewModelProvider(this, factory).get(TimeEntryViewModel.class);
        timeEntryViewModel.getTimeEntries().observe(this, timeEntries -> adapter.setEntries(timeEntries));
    }
}
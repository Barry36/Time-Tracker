package com.cs446.group18.timetracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.cs446.group18.timetracker.adapter.EventListAdapter;
import com.cs446.group18.timetracker.utils.InjectorUtils;
import com.cs446.group18.timetracker.vm.EventListViewModelFactory;
import com.cs446.group18.timetracker.vm.EventViewModel;

public class MainActivity extends AppCompatActivity {

    private EventViewModel eventViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        final EventListAdapter adapter = new EventListAdapter();
        recyclerView.setAdapter(adapter);

        EventListViewModelFactory factory = InjectorUtils.provideEventListViewModelFactory(getApplicationContext());
        eventViewModel = new ViewModelProvider(this, factory).get(EventViewModel.class);
        eventViewModel.getEvents().observe(this, events -> adapter.setEvents(events));
    }
}
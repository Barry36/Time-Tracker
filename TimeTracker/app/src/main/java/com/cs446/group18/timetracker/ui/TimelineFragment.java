package com.cs446.group18.timetracker.ui;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.cs446.group18.timetracker.R;
import com.cs446.group18.timetracker.adapter.EventListAdapter;
import com.cs446.group18.timetracker.adapter.TimeLineAdapter;
import com.cs446.group18.timetracker.entity.Event;
import com.cs446.group18.timetracker.entity.TimeEntry;
import com.cs446.group18.timetracker.model.TimeLineModel;
import com.cs446.group18.timetracker.utils.AbstractFactory;
import com.cs446.group18.timetracker.utils.ConcreteFactory;
import com.cs446.group18.timetracker.vm.EventListViewModelFactory;
import com.cs446.group18.timetracker.vm.EventViewModel;
import com.cs446.group18.timetracker.vm.TimeEntryListViewModelFactory;
import com.cs446.group18.timetracker.vm.TimeEntryViewModel;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimelineFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimelineFragment extends Fragment {

    private EventListAdapter adapter;
    private HashMap<Long, String> eventMap = new HashMap<>();
    private HashMap<Long, Event> events = new HashMap<>();
    private List<TimeEntry> timeEntries = new ArrayList<>();
    private RecyclerView mRecycler;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    private AbstractFactory factory = new ConcreteFactory();

    public TimelineFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment TimelineFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static TimelineFragment newInstance(String param1, String param2) {
        TimelineFragment fragment = new TimelineFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);
        mRecycler = (RecyclerView) view.findViewById(R.id.time_line_recycler);
        TimeLineAdapter adapter = initRecycler();
        TextView header = view.findViewById(R.id.timeline_header);

        EventListViewModelFactory eventFactory = factory.provideEventListViewModelFactory(getActivity());
        EventViewModel eventViewModel = new ViewModelProvider(this, eventFactory).get(EventViewModel.class);
        eventViewModel.getEvents().observe(getViewLifecycleOwner(), new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable List<Event> events) {

                if (events != null && !events.isEmpty()) {
                    header.setText(R.string.timeline_start);
                } else {
                    header.setText(R.string.timeline_no_events);
                }
                setEvents(events);
                setEventMap(events);
            }
        });

        TimeEntryListViewModelFactory timeEntryListViewModelFactory = factory.provideTimeEntryListViewModelFactory(getActivity());
        TimeEntryViewModel factoryViewModel = new ViewModelProvider(this, timeEntryListViewModelFactory).get(TimeEntryViewModel.class);

        factoryViewModel.getTimeEntries().observe(getViewLifecycleOwner(), new Observer<List<TimeEntry>>() {
            @Override
            public void onChanged(@Nullable List<TimeEntry> timeEntries) {

                if (timeEntries != null && !timeEntries.isEmpty()) {
                    header.setText(R.string.timeline_start);
                } else {
                    header.setText(R.string.timeline_no_events);
                }
                setTimeEntries(timeEntries);
                List<TimeLineModel> models = getData();
                adapter.setItems(models);
                mRecycler.setAdapter(adapter);
            }
        });

        super.onCreate(savedInstanceState);

        return view;
    }

    private TimeLineAdapter initRecycler() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(layoutManager);

        TimeLineAdapter adapter = new TimeLineAdapter();
        return adapter;
    }

    private List<TimeLineModel> getData() {
        List<TimeLineModel> models = new ArrayList<TimeLineModel>();
        SimpleDateFormat datetimeformat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        SimpleDateFormat timeformat = new SimpleDateFormat("HH:mm");

        Collections.reverse(timeEntries);
        timeEntries.forEach((timeEntry -> { models.add(new TimeLineModel(eventMap.get(timeEntry.getEventId()), datetimeformat.format(timeEntry.getStartTime()) + " to " + timeformat.format(timeEntry.getEndTime()), events.get(timeEntry.getEventId()))); }));

//        models.add(new TimeLineModel("Study for 446", "2016-08-09"));
//        models.add(new TimeLineModel("Study for 452", "2016-08-09"));
//        models.add(new TimeLineModel("452 Final exam", "2016-08-10"));
//        models.add(new TimeLineModel("Driving test", "2016-08-10"));
//        models.add(new TimeLineModel("Optometrist appointment", "2016-08-12"));
//        models.add(new TimeLineModel("Study for 458", "2016-08-15"));
//        models.add(new TimeLineModel("457A Final exam ", "2016-08-19"));
//        models.add(new TimeLineModel("458 Final Exam", "2016-08-20"));
//        models.add(new TimeLineModel("End of term!", "2016-08-21"));

        return models;
    }

    private void setEventMap(List<Event> eventMap) {
        eventMap.forEach((event -> {
            this.eventMap.put(event.getEventId(), event.getEventName());
        }));
    }

    private void setEvents(List<Event> events) {
        events.forEach((event -> {
            this.events.put(event.getEventId(), event);
        }));
    }

    private void setTimeEntries(List<TimeEntry> timeEntries) {
        this.timeEntries = timeEntries;
    }
}
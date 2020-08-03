package com.cs446.group18.timetracker.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import com.cs446.group18.timetracker.R;
import com.cs446.group18.timetracker.adapter.EventListAdapter;
import com.cs446.group18.timetracker.entity.Event;
import com.cs446.group18.timetracker.entity.dateSelected;
import com.cs446.group18.timetracker.utils.InjectorUtils;
import com.cs446.group18.timetracker.vm.EventListViewModelFactory;
import com.cs446.group18.timetracker.vm.EventViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment implements EventListAdapter.OnEventListener {

    private dateSelected callback;
    private EventListAdapter adapter;
    private List<Event> events = new ArrayList<>();
    RecyclerView recyclerView;
    private TextView textViewEmpty;
    private FloatingActionButton buttonAddEvent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        // Calendar
        CalendarView calendarView = view.findViewById(R.id.calendar_view);
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                String newYear = "" + year;
                String newMonth = "00";
                String date = "00";

                if(dayOfMonth < 10) {
                    date = "0" + dayOfMonth;
                } else {
                    date = "" + dayOfMonth;
                }

                int i = month;
                i++;

                if(month < 10) {
                    newMonth = "0" + i;
                } else {
                    newMonth = "" + i;
                }

                callback.itemSelected(newYear, newMonth, date);
            }
        });
        callback = (dateSelected) getActivity();

        // Add Event Button
        Button button = (Button) view.findViewById(R.id.add_event);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try {
                    Intent i = new Intent(getActivity(), AddEvent.class);
                    startActivity(i);
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
        });

        // Event List
        EventListAdapter eventListAdapter = new EventListAdapter(events, this);
        EventListAdapter adapter = new EventListAdapter(events, this);
        this.adapter = adapter;

        EventListViewModelFactory factory = InjectorUtils.provideEventListViewModelFactory(getActivity());
        EventViewModel viewModel = new ViewModelProvider(this, factory).get(EventViewModel.class);

        textViewEmpty = view.findViewById(R.id.calendar_empty_event_list);
        recyclerView = view.findViewById(R.id.calendar_event_list);
        recyclerView.setAdapter(eventListAdapter);

        subscribeUI(eventListAdapter);
        return view;
    }

    private void subscribeUI(EventListAdapter eventListAdapter) {

        EventListViewModelFactory factory = InjectorUtils.provideEventListViewModelFactory(getActivity());
        EventViewModel viewModel = new ViewModelProvider(this, factory).get(EventViewModel.class);
        viewModel.getEvents().observe(getViewLifecycleOwner(), new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable List<Event> events) {

                if (events != null && !events.isEmpty()) {
                    recyclerView.setVisibility(View.VISIBLE);
                    textViewEmpty.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    textViewEmpty.setVisibility(View.VISIBLE);
                }
                setEvents(events);
                eventListAdapter.setEvents(events);
            }
        });
    }

    @Override
    public void onEventClick(int position, boolean isFromNFC) {
        // TODO:
    }

    private void setEvents(List<Event> events) {
        this.events = events;
    }



    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CalendarFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CalendarFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CalendarFragment newInstance(String param1, String param2) {
        CalendarFragment fragment = new CalendarFragment();
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
}
package com.cs446.group18.timetracker.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

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


public class CalendarFragment extends Fragment implements EventListAdapter.OnEventListener{

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

//        // Add New Event
//        buttonAddEvent = view.findViewById(R.id.button_add_event);
//        buttonAddEvent.setOnClickListener(v -> {
//            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.DialogTheme);
//            View promptView = inflater.inflate(R.layout.prompt_add_event, container, false);
//
//            final EditText eventNameText = promptView.findViewById(R.id.event_name);
//            final EditText eventDescriptionText = promptView.findViewById(R.id.event_description);
//            builder.setView(promptView)
//                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id) {
//                            String eventName = eventNameText.getText().toString();
//                            String eventDescription = eventDescriptionText.getText().toString();
//                            try {
//                                viewModel.insert(new Event(eventName, eventDescription));
//                                Toast.makeText(view.getContext(), "Add new event: " + eventName, Toast.LENGTH_SHORT).show();
//                            } catch (Exception e) {
//                                dialog.dismiss();
//                                AlertDialog.Builder errorBuilder = new AlertDialog.Builder(getActivity());
//                                errorBuilder.setPositiveButton("OK",
//                                        new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog, int id) {
//                                                dialog.dismiss();
//                                            }
//                                        });
//                                errorBuilder.setTitle("Error");
//                                errorBuilder.setMessage("Please enter a valid input");
//                                AlertDialog error = errorBuilder.create();
//                                error.show();
//                            }
//                        }
//                    })
//                    .setNegativeButton("Cancel",
//                            new DialogInterface.OnClickListener() {
//                                public void onClick(DialogInterface dialog, int id) {
//                                    dialog.cancel();
//                                }
//                            });
//
//            AlertDialog dialog = builder.create();
//            dialog.show();
//        });

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
    public void onEventClick(int position) {
        // TODO: Go to edit event page?
    }

    private void setEvents(List<Event> events) {
        this.events = events;
    }
}

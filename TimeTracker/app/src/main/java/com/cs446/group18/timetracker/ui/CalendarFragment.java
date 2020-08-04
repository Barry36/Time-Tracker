package com.cs446.group18.timetracker.ui;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cs446.group18.timetracker.R;
import com.cs446.group18.timetracker.adapter.EventListAdapter;
import com.cs446.group18.timetracker.entity.DateSelected;
import com.cs446.group18.timetracker.entity.Event;
import com.cs446.group18.timetracker.entity.TimeEntry;
import com.cs446.group18.timetracker.utils.InjectorUtils;
import com.cs446.group18.timetracker.vm.EventListViewModelFactory;
import com.cs446.group18.timetracker.vm.EventViewModel;
import com.cs446.group18.timetracker.vm.TimeEntryListViewModelFactory;
import com.cs446.group18.timetracker.vm.TimeEntryViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CalendarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CalendarFragment extends Fragment implements EventListAdapter.OnEventListener {

    private EventListAdapter adapter;
    private List<Event> events = new ArrayList<>();
    private List<Event> dayEvents = new ArrayList<>();
    private List<TimeEntry> timeEntries = new ArrayList<>();
    private List<TimeEntry> dayEntries = new ArrayList<>();
    private RecyclerView recyclerView;
    private TextView textViewEmpty;
    private int position;
    private int prevPosition;
    private Date dateSelected;

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

                dateSelected = new Date();
                itemSelected(year, month, dayOfMonth);
            }
        });

        // Event List
        EventListAdapter adapter = new EventListAdapter(events, this);
        this.adapter = adapter;

        EventListViewModelFactory factory = InjectorUtils.provideEventListViewModelFactory(getActivity());
        EventViewModel viewModel = new ViewModelProvider(this, factory).get(EventViewModel.class);

        textViewEmpty = view.findViewById(R.id.calendar_empty_event_list);
        recyclerView = view.findViewById(R.id.calendar_event_list);
        recyclerView.setAdapter(adapter);


        subscribeUI(adapter);
        return view;
    }

    private void subscribeUI(EventListAdapter eventListAdapter) {

        TimeEntryListViewModelFactory tfactory = InjectorUtils.provideTimeEntryListViewModelFactory(getActivity());
        TimeEntryViewModel tviewModel = new ViewModelProvider(this, tfactory).get(TimeEntryViewModel.class);
        tviewModel.getTimeEntries().observe(getViewLifecycleOwner(), new Observer<List<TimeEntry>>() {
            @Override
            public void onChanged(@Nullable List<TimeEntry> timeEntries) {

                setTimeEntries(timeEntries);
            }
        });

        EventListViewModelFactory factory = InjectorUtils.provideEventListViewModelFactory(getActivity());
        EventViewModel viewModel = new ViewModelProvider(this, factory).get(EventViewModel.class);
        viewModel.getEvents().observe(getViewLifecycleOwner(), new Observer<List<Event>>() {
            @Override
            public void onChanged(@Nullable List<Event> events) {

                List<Event> dayEvents = new ArrayList<>();

                if (events != null) {
                    events.forEach(event -> {
                        timeEntries.forEach(timeEntry -> {
                            if (isSameDay(timeEntry.getStartTime(), dateSelected)) {
                                dayEvents.add(event);
                            }
                        });
                    });
                }

                if (!dayEvents.isEmpty()) {
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

    // Expandable CardView
    @SuppressLint("RestrictedApi")
    @Override
    public void onEventClick(int position, boolean isFromNFC) {

        setRecyclerView(recyclerView);
        this.position = position;
        // Time Entries
        long eventID = events.get(position).getEventId();

        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        View view = linearLayoutManager.findViewByPosition(position);
        LinearLayout expandableLinearLayout = view.findViewById(R.id.expandable);

        View preView = linearLayoutManager.findViewByPosition(prevPosition);
        LinearLayout preExpandableLinearLayout = view.findViewById(R.id.expandable);


        TimeEntryListViewModelFactory timeEntryListViewModelFactory = InjectorUtils.provideTimeEntryListViewModelFactory((getActivity()));
        TimeEntryViewModel timeEntryViewModel = new ViewModelProvider(this, timeEntryListViewModelFactory).get(TimeEntryViewModel.class);
        timeEntryViewModel.getTimeEntriesByEventID(eventID).observe(getViewLifecycleOwner(), new Observer<List<TimeEntry>>() {
            @Override
            public void onChanged(List<TimeEntry> timeEntries) {
                if(expandableLinearLayout.getChildCount() > 0){
                    expandableLinearLayout.removeAllViews();
                }
                for (int i = 0; i < timeEntries.size(); ++i) {
                    if (dateSelected == null) {
                        dateSelected = new Date();
                    }

                    if (isSameDay(timeEntries.get(i).getStartTime(), dateSelected)) {
                        int duration = (int) timeEntries.get(i).getDuration() / 1000;
                        String text = "Duration: " + Integer.toString(duration) + " second";
                        TextView textView = new TextView(getContext());
                        textView.setText(text);
                        textView.setId(i);
                        expandableLinearLayout.addView(textView);
                    }

                }
                adapter.setTimeEntries(timeEntries);
            }
        });
        if (expandableLinearLayout.getVisibility() == View.GONE) {
            expand(expandableLinearLayout);
            prevPosition = position;

        } else {
            collapse(expandableLinearLayout);
        }

    }

    private void setEvents(List<Event> events) {
        this.events = events;
    }

    private void setDayEvents(List<Event> events) {
        this.dayEvents = events;
    }

    private void setTimeEntries(List<TimeEntry> timeEntries) {
        this.timeEntries = timeEntries;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }

    private void expand(LinearLayout layout) {
        layout.setVisibility(View.VISIBLE);
    }

    public void collapse(LinearLayout layout) {
        layout.setVisibility(View.GONE);
    }

    public void itemSelected(final Integer year, final Integer month, final Integer dayOfMonth) {

        this.dateSelected = new GregorianCalendar(year, month, dayOfMonth).getTime();

        dayEntries.clear();
        timeEntries.forEach(timeEntry -> {
            if (isSameDay(timeEntry.getStartTime(), dateSelected)) {
                dayEntries.add(timeEntry);
            }
        });

        setTimeEntries(timeEntries);
        adapter.setTimeEntries(dayEntries);

        List<Event> dayEvents = new ArrayList<>();

        if (events != null) {
            events.forEach(event -> {
                timeEntries.forEach(timeEntry -> {
                    if (isSameDay(timeEntry.getStartTime(), dateSelected)) {
                        dayEvents.add(event);
                    }
                });
            });
        }

        if (!dayEvents.isEmpty()) {
            recyclerView.setVisibility(View.VISIBLE);
            textViewEmpty.setVisibility(View.GONE);

            LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        } else {
            recyclerView.setVisibility(View.GONE);
            textViewEmpty.setVisibility(View.VISIBLE);
        }

        setEvents(events);
        setDayEvents(dayEvents);
        adapter.setEvents(events);
    }

    private static boolean isSameDay(Date date1, Date date2) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.CANADA);
        return sdf.format(date1).equals(sdf.format(date2));
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
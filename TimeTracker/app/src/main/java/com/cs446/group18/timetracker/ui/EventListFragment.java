package com.cs446.group18.timetracker.ui;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cs446.group18.timetracker.R;
import com.cs446.group18.timetracker.adapter.EventListAdapter;
import com.cs446.group18.timetracker.adapter.IconListAdaptor;
import com.cs446.group18.timetracker.entity.Event;
import com.cs446.group18.timetracker.entity.TimeEntry;
import com.cs446.group18.timetracker.utils.InjectorUtils;
import com.cs446.group18.timetracker.vm.EventListViewModelFactory;
import com.cs446.group18.timetracker.vm.EventViewModel;
import com.cs446.group18.timetracker.vm.TimeEntryListViewModelFactory;
import com.cs446.group18.timetracker.vm.TimeEntryViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EventListFragment extends Fragment implements EventListAdapter.OnEventListener {
    private EventListAdapter adapter;
    private List<Event> events = new ArrayList<>();
    RecyclerView recyclerView;
    private TextView textViewEmpty;
    private FloatingActionButton buttonAddEvent;
    private int position;
    private int prevPosition;
    private final ArrayList<Integer> iconList = new ArrayList<>(Arrays.asList(R.drawable.ic_cooking, R.drawable.ic_yoga, R.drawable.ic_homework, R.drawable.ic_movies, R.drawable.ic_music, R.drawable.ic_soccer,
            R.drawable.ic_gym, R.drawable.ic_cafe, R.drawable.ic_cleaning, R.drawable.ic_coffee, R.drawable.ic_shopping_cart, R.drawable.ic_task, R.drawable.ic_television, R.drawable.ic_youtube, R.drawable.ic_sms));


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View eventListView = inflater.inflate(R.layout.fragment_event_list, container, false);

        EventListAdapter eventListAdapter = new EventListAdapter(events, this);


        EventListAdapter adapter = new EventListAdapter(events, this);
        this.adapter = adapter;

        EventListViewModelFactory factory = InjectorUtils.provideEventListViewModelFactory(getActivity());
        EventViewModel viewModel = new ViewModelProvider(this, factory).get(EventViewModel.class);

        textViewEmpty = eventListView.findViewById(R.id.empty_event_list);
        recyclerView = eventListView.findViewById(R.id.event_list);
        recyclerView.setAdapter(eventListAdapter);


        // Add New Event
        buttonAddEvent = eventListView.findViewById(R.id.button_add_event);
        buttonAddEvent.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                View promptView = inflater.inflate(R.layout.prompt_add_event, container, false);
                //create icon list view
                final GridView list = promptView.findViewById(R.id.iconList);
                IconListAdaptor iconAdapter = new IconListAdaptor(promptView.getContext(), R.layout.list_item_icon, iconList);
                list.setAdapter(iconAdapter);

                final EditText eventNameText = promptView.findViewById(R.id.event_name);
                final EditText eventDescriptionText = promptView.findViewById(R.id.event_description);
                builder.setView(promptView)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                String eventName = eventNameText.getText().toString();
                                String eventDescription = eventDescriptionText.getText().toString();
                                // get selected icon
                                int selectedIcon = iconAdapter.getSelected();
                                try {
                                    viewModel.insert(new Event(eventName, eventDescription, selectedIcon));
                                    Toast.makeText(eventListView.getContext(), "Add new event: " + eventName, Toast.LENGTH_SHORT).show();
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


        // Swipe Right to Delete Event & Swipe Left to Update
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.RIGHT) {
                    viewModel.delete(eventListAdapter.getEventAt(viewHolder.getAdapterPosition()));
                    Toast.makeText(eventListView.getContext(), "Event Deleted", Toast.LENGTH_SHORT).show();
                } else if (direction == ItemTouchHelper.LEFT) {
                    long eventId = eventListAdapter.getEventAt(viewHolder.getAdapterPosition()).getEventId();
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    View promptView = inflater.inflate(R.layout.prompt_add_event, container, false);

                    //create icon list view
                    final GridView list = promptView.findViewById(R.id.iconList);
                    IconListAdaptor iconAdapter = new IconListAdaptor(promptView.getContext(), R.layout.list_item_icon, iconList);
                    list.setAdapter(iconAdapter);


                    final EditText eventNameText = promptView.findViewById(R.id.event_name);
                    final EditText eventDescriptionText = promptView.findViewById(R.id.event_description);
                    builder.setView(promptView)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    int pos = viewHolder.getAdapterPosition();
                                    String eventName = eventNameText.getText().toString();
                                    String eventDescription = eventDescriptionText.getText().toString();
                                    // get selected icon
                                    int selectedIcon = iconAdapter.getSelected();
                                    try {
                                        Event event = new Event(eventName, eventDescription, selectedIcon);
                                        event.setEventId(eventId);
                                        viewModel.update(event);
                                        Toast.makeText(eventListView.getContext(), "Event Update", Toast.LENGTH_SHORT).show();

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
                                            TextView eventNameTextView = viewHolder.itemView.findViewById(R.id.text_view_title);
                                            String eventName = eventNameTextView.getText().toString();
                                            TextView eventDescriptionTextView = viewHolder.itemView.findViewById(R.id.text_view_description);
                                            String eventDescription = eventDescriptionTextView.getText().toString();
                                            // get selected icon
                                            Event temp = eventListAdapter.getEventAt(viewHolder.getAdapterPosition());
                                            int iconId = temp.getIcon();
                                            Event event = new Event(eventName, eventDescription, iconId);
                                            event.setEventId(eventId);
                                            viewModel.update(event);
                                            dialog.cancel();
                                        }
                                    });

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }
        }).attachToRecyclerView(recyclerView);


        subscribeUI(eventListAdapter);
        return eventListView;
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


    // Expandable CardView
    @SuppressLint("RestrictedApi")
    @Override
    public void onEventClick(int position) {

        setRecyclerView(recyclerView);
        this.position = position;
        // Time Entries
        long eventID = events.get(position).getEventId();

        //integration to stopwatch, should be changed later
        FragmentTransaction ft = getChildFragmentManager().beginTransaction();
        StopwatchFragment stopwatchFragment = StopwatchFragment.newInstance(eventID);
        ft.replace(R.id.stopwatch, stopwatchFragment);
        ft.commit();


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
                Log.d("EventListFragment", "onChanged is called");
                if (expandableLinearLayout.getChildCount() > 0) {
                    expandableLinearLayout.removeAllViews();
                }
                for (int i = 0; i < timeEntries.size(); ++i) {
                    int duration = (int) timeEntries.get(i).getDuration() / 1000;
                    String text = "Duration: " + Integer.toString(duration) + " second";
                    TextView textView = new TextView(getContext());
                    textView.setText(text);
                    textView.setId(i);
                    expandableLinearLayout.addView(textView);
                }
                adapter.setTimeEntries(timeEntries);
            }
        });
        if (expandableLinearLayout.getVisibility() == View.GONE) {
            expand(expandableLinearLayout);

            prevPosition = position;
            buttonAddEvent.setVisibility(View.GONE);
        } else {
            FragmentTransaction closeFt = getChildFragmentManager().beginTransaction();
            closeFt.remove(stopwatchFragment).commit();
            if (expandableLinearLayout.getChildCount() > 0) {
                expandableLinearLayout.removeAllViews();
            }
            collapse(expandableLinearLayout);
            buttonAddEvent.setVisibility(View.VISIBLE);
        }

    }


    private void expand(LinearLayout layout) {
        layout.setVisibility(View.VISIBLE);
    }


    public void collapse(LinearLayout layout) {
        layout.setVisibility(View.GONE);
    }

    private void setEvents(List<Event> events) {
        this.events = events;
    }

    public int getPosition() {
        return this.position;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setRecyclerView(RecyclerView recyclerView) {
        this.recyclerView = recyclerView;
    }
}

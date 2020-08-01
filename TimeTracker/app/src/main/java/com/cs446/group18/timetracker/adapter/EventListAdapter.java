package com.cs446.group18.timetracker.adapter;

import android.app.admin.ConnectEvent;
import android.media.MediaDrm;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cs446.group18.timetracker.R;
import com.cs446.group18.timetracker.databinding.ListItemEventBinding;
import com.cs446.group18.timetracker.entity.Event;
import com.cs446.group18.timetracker.entity.TimeEntry;
import com.cs446.group18.timetracker.utils.InjectorUtils;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {
    private List<Event> events = new ArrayList<>();
    private List<TimeEntry> timeEntries = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private OnEventListener onEventListener;
    ListItemEventBinding binding;

    public EventListAdapter(List<Event> events, OnEventListener onEventListener) {

        this.events = events;
        this.onEventListener = onEventListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext()); // get context from main Activity
        }
        ListItemEventBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item_event, parent, false);

        return new ViewHolder(binding, onEventListener);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event currentEvent = getEventAt(position);
        ImageView imageIcon = holder.itemView.findViewById(R.id.imageView);
        if (imageIcon != null){
            imageIcon.setImageResource(currentEvent.getIcon());
        }
        holder.bind(currentEvent);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    // get List of Event LiveData
    public void setEvents(List<Event> events) {
        this.events = events;
        // there's more efficient way to update adapter
        notifyDataSetChanged();
    }

    public void setTimeEntries(List<TimeEntry> timeEntries) {
        this.timeEntries = timeEntries;
        // there's more efficient way to update adapter
        notifyDataSetChanged();
    }

    public Event getEventAt(int position) {
        return events.get(position);
    }


    // This is the card view
    // Detect the click by implementing onClickListener
    class ViewHolder extends RecyclerView.ViewHolder {
        private ListItemEventBinding binding;
        private OnEventListener onEventListener;
        private long eventID;

        public ViewHolder(@NonNull ListItemEventBinding binding, OnEventListener onEventListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.onEventListener = onEventListener;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    onEventListener.onEventClick(getAdapterPosition());
//                    eventID = events.get(getAdapterPosition()).getEventId();
                }
            });
        }


            void bind(Event event){
                // access data variable in list_item_event.xml
                binding.setEvent(event);
                binding.executePendingBindings();
            }
        }


        // Interface for the itemView onClick Listener
        // Implemented in Activity - EventListFragment.java to handle unfold action

        public interface OnEventListener {
            void onEventClick(int position);
        }


    public View getView(){
        return binding.getRoot();
    }

    private void setBing(ListItemEventBinding binding){
        this.binding = binding;
    }

}


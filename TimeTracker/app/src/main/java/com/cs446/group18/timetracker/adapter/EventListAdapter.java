package com.cs446.group18.timetracker.adapter;

import android.app.admin.ConnectEvent;
import android.media.MediaDrm;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cs446.group18.timetracker.R;
import com.cs446.group18.timetracker.databinding.ListItemEventBinding;
import com.cs446.group18.timetracker.entity.Event;
import com.cs446.group18.timetracker.utils.InjectorUtils;
import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {
    private List<Event> events;
    private LayoutInflater layoutInflater;

    public EventListAdapter(List<Event> events) {
        this.events = events;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext()); // get context from main Activity
        }
        ListItemEventBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item_event, parent, false);

        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event currentEvent = getEventAt(position);
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


    public Event getEventAt(int position) { return events.get(position); }


    // This is the card view
    // Detect the click by implementing onClickListener
    class ViewHolder extends RecyclerView.ViewHolder {
        private ListItemEventBinding binding;
        private long eventID;
        public ViewHolder(@NonNull ListItemEventBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(new View.OnClickListener(){

                @Override
                public void onClick(View v) {
                    eventID = events.get(getAdapterPosition()).getEventId();
                    if (binding.expandable.getVisibility() == View.GONE) {
                        expand(binding.expandable);

                    } else {
                        collapse(binding.expandable);
                    }
                }
            });
        }

        void bind(Event event) {
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

//    private void expand(RelativeLayout layout) {
//        layout.setVisibility(View.VISIBLE);
//    }
    private void expand(RecyclerView view) {
        view.setVisibility(View.VISIBLE);
    }

//    private void collapse(final RelativeLayout layout) {
//        layout.setVisibility(View.GONE);
//    }

    private void collapse(RecyclerView view) {
        view.setVisibility(View.GONE);
    }
}

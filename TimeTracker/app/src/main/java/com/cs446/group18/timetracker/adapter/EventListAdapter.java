package com.cs446.group18.timetracker.adapter;

import android.app.admin.ConnectEvent;
import android.media.MediaDrm;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cs446.group18.timetracker.R;
import com.cs446.group18.timetracker.databinding.ListItemEventBinding;
import com.cs446.group18.timetracker.entity.Event;

import java.util.List;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.ViewHolder> {
    private List<Event> events;
    private LayoutInflater layoutInflater;
    private OnEventListener mOnEventListener;

    public EventListAdapter(List<Event> events, OnEventListener onEventListener) {
        this.events = events;
        this.mOnEventListener = onEventListener;    // makes clickListener visible to viewHolder
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext()); // get context from main Activity
        }
        ListItemEventBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item_event, parent, false);
        return new ViewHolder(binding, mOnEventListener);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Event currentEvent = events.get(position);
        holder.bind(currentEvent);

        // holder.bind(currentEvent, onEventListener);
    }

    @Override
    public int getItemCount() {
        return events.size();
    }

    // get List of LiveData
    public void setEvents(List<Event> events) {
        this.events = events;
        // there's more efficient way to update adapter
        notifyDataSetChanged();
    }

    public Event getEventAt(int position){
        return events.get(position);
    }


    // This is the card view
    // Detect the click by implementing onClickListener
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private ListItemEventBinding binding;
        private OnEventListener onEventListener;

        public ViewHolder(@NonNull ListItemEventBinding binding, OnEventListener onEventListener) {
            super(binding.getRoot());
            this.binding = binding;
            this.onEventListener = onEventListener;
            binding.getRoot().setOnClickListener(this);
        }

        void bind(Event event) {
            // access data variable in list_item_event.xml
            binding.setEvent(event);
            binding.executePendingBindings();
        }

        @Override
        public void onClick(View v) {
            onEventListener.onEventClick(getAdapterPosition());
        }
    }

    // Interface for the itemView onClick Listener
    // Implemented in Activity - EventListFragment.java to handle unfold action

    public interface OnEventListener{
        void onEventClick(int position);
    }

}

package com.cs446.group18.timetracker.adapter;

import android.view.LayoutInflater;
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
    private onItemClickListener listener;

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
        Event currentEvent = events.get(position);
        holder.bind(currentEvent);
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
    class ViewHolder extends RecyclerView.ViewHolder {
        private ListItemEventBinding binding;

        ViewHolder(@NonNull ListItemEventBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Event event) {
            binding.setEvent(event);
            binding.executePendingBindings();
        }

    }

    public interface onItemClickListener{
        void onItemClick(Event event);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        this.listener = listener;
    }
}

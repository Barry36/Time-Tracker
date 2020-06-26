package com.cs446.group18.timetracker.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cs446.group18.timetracker.R;
import com.cs446.group18.timetracker.entity.TimeEntry;
import com.cs446.group18.timetracker.util.DateTimeConverter;

import java.util.ArrayList;
import java.util.List;

public class TimeEntryAdapter extends RecyclerView.Adapter<TimeEntryAdapter.ViewHolder> {
    private List<TimeEntry> timeEntries = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()) // get context from main Activity
                .inflate(R.layout.entry_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TimeEntry currentEntry = timeEntries.get(position);
        holder.textViewTitle.setText(DateTimeConverter.dateToTimestamp(currentEntry.getStartTime()));
        holder.textViewDescription.setText("Duration: " + currentEntry.getDuration());
    }

    @Override
    public int getItemCount() {
        return timeEntries.size();
    }

    // get List of LiveData
    public void setEntries(List<TimeEntry> timeEntries) {
        this.timeEntries = timeEntries;
        // there's more efficient way to update adapter
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewTitle;
        private TextView textViewDescription;
        public ViewHolder(View itemView) {
            super(itemView);
            textViewTitle = itemView.findViewById(R.id.text_view_title);
            textViewDescription = itemView.findViewById(R.id.text_view_description);
        }
    }

}

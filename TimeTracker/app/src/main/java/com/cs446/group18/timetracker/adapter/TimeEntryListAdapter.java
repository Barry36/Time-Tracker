package com.cs446.group18.timetracker.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cs446.group18.timetracker.R;
import com.cs446.group18.timetracker.databinding.ListItemEventBinding;
import com.cs446.group18.timetracker.entity.Event;
import com.cs446.group18.timetracker.entity.TimeEntry;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

public class TimeEntryListAdapter extends RecyclerView.Adapter<TimeEntryListAdapter.timeEntryHolder> {
    private List<TimeEntry> timeEntries = new ArrayList<>();
    private LayoutInflater layoutInflater;

    @NonNull
    @Override
    public timeEntryHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_time_entry,parent,false);
        return new timeEntryHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull timeEntryHolder holder, int position) {
        Log.d("yoo", "onBindViewHolder: called");
        TimeEntry currentTimeEntry = timeEntries.get(position);
        holder.textViewStartTime.setText(currentTimeEntry.getStartTimeStr());
        holder.textViewEndTime.setText(currentTimeEntry.getEndTimeStr());
        holder.textViewDurationTime.setText(currentTimeEntry.getDurationStr());
    }

    @Override
    public int getItemCount() {
        return timeEntries.size();
    }


    public void setTimeEntries(List<TimeEntry> timeEntries){
        this.timeEntries = timeEntries;
        notifyDataSetChanged();
    }

    class timeEntryHolder extends RecyclerView.ViewHolder {
        private TextView textViewStartTime;
        private TextView textViewEndTime;
        private TextView textViewDurationTime;


        public timeEntryHolder(@NonNull View itemView) {
            super(itemView);
            textViewStartTime = itemView.findViewById(R.id.time_entry_startTime);
            textViewEndTime = itemView.findViewById(R.id.time_entry_endTime);
            textViewDurationTime = itemView.findViewById(R.id.time_entry_duration);

        }

    }

}

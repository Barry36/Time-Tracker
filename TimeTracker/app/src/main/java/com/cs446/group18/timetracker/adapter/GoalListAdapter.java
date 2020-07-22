package com.cs446.group18.timetracker.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.cs446.group18.timetracker.R;
import com.cs446.group18.timetracker.databinding.ListItemGoalBinding;
import com.cs446.group18.timetracker.entity.Goal;

import java.util.List;

public class GoalListAdapter extends RecyclerView.Adapter<GoalListAdapter.ViewHolder> {
    private List<Goal> goals;
    private LayoutInflater layoutInflater;

    public GoalListAdapter(List<Goal> goals) {
        this.goals = goals;
    }

    @NonNull
    @Override
    public GoalListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (layoutInflater == null) {
            layoutInflater = LayoutInflater.from(parent.getContext()); // get context from main Activity
        }
        ListItemGoalBinding binding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item_goal, parent, false);
        return new GoalListAdapter.ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull GoalListAdapter.ViewHolder holder, int position) {
        Goal currentGoal = goals.get(position);
        holder.bind(currentGoal);
    }

    @Override
    public int getItemCount() {
        return goals.size();
    }

    // get List of LiveData
    public void setGoals(List<Goal> goals) {
        this.goals = goals;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private ListItemGoalBinding binding;

        ViewHolder(@NonNull ListItemGoalBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(Goal goal) {
            binding.setGoal(goal);
            binding.executePendingBindings();
        }
    }
}

package com.cs446.group18.timetracker.vm;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.cs446.group18.timetracker.repository.EventRepository;

public class EventListViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private EventRepository eventRepository;

    public EventListViewModelFactory(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        EventViewModel eventViewModel = new EventViewModel(eventRepository);
        return (T) eventViewModel;
    }
}

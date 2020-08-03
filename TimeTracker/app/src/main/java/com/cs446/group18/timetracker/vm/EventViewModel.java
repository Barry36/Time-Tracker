package com.cs446.group18.timetracker.vm;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.cs446.group18.timetracker.entity.Event;
import com.cs446.group18.timetracker.repository.EventRepository;

import java.util.List;

public class EventViewModel extends ViewModel {
    private EventRepository repository;

    // ViewModel can survive after the activity is destroyed
    // Repository needs a context to instantiate the database,
    // but if we reference activity context, it will cause memory leak
    public EventViewModel(EventRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<Event>> getEvents() {
        return repository.getEvents();
    }

    public Event getEventById(long id) {
        return repository.getEventById(id);
    }

    public void insert(Event event) {
        repository.createEvent(event);
    }

    public void delete(Event event) { repository.deleteEvent(event); }

    public void deleteAll() { repository.deleteAllEvents(); }

    public void update(Event event) { repository.updateEvent(event); }

}

package com.cs446.group18.timetracker.repository;

import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.cs446.group18.timetracker.dao.EventDao;
import com.cs446.group18.timetracker.entity.Event;

import java.util.List;

public class EventRepository {
    private EventDao eventDao;
    private static volatile EventRepository instance = null;

    public EventRepository(EventDao eventDao) {
        this.eventDao = eventDao;
    }

    public static EventRepository getInstance(EventDao eventDao) {
        if (instance == null) {
            synchronized (EventRepository.class) {
                if (instance == null)
                    instance = new EventRepository(eventDao);
            }
        }
        return instance;
    }

    public LiveData<List<Event>> getEvents() {
        return eventDao.getEvents();
    }

    public void createEvent(Event event) {
        AsyncTask.execute(() -> eventDao.insert(event));
    }

    public void deleteEvent(Event event){ AsyncTask.execute(() -> eventDao.delete(event));}

    public void deleteAllEvents(){ AsyncTask.execute(() -> eventDao.deleteAllEvents());}

    public void updateEvent(Event event){ AsyncTask.execute(() -> eventDao.update(event));}
}

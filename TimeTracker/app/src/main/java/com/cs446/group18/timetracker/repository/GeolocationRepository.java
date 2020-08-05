package com.cs446.group18.timetracker.repository;

import android.os.AsyncTask;

import com.cs446.group18.timetracker.dao.GeolocationDao;
import com.cs446.group18.timetracker.entity.Geolocation;

import java.util.List;

public class GeolocationRepository {
    private GeolocationDao geolocationDao;
    private static volatile GeolocationRepository instance = null;

    private GeolocationRepository(GeolocationDao geolocationDao) {
        this.geolocationDao = geolocationDao;
    }

    public static GeolocationRepository getInstance(GeolocationDao geolocationDao) {
        if (instance == null) {
            synchronized (GeolocationRepository.class) {
                if (instance == null)
                    instance = new GeolocationRepository(geolocationDao);
            }
        }
        return instance;
    }

    public List<Geolocation> getGeolocations() {
        return geolocationDao.getGeolocations();
    }



    public Geolocation getGeolocationById(long id) {
        return geolocationDao.getGeolocationById(id);
    }

    public void createGeolocation(Geolocation geolocation) {
        AsyncTask.execute(() -> geolocationDao.insert(geolocation));
    }
}

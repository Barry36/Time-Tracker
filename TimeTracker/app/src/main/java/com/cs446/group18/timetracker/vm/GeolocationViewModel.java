package com.cs446.group18.timetracker.vm;

import androidx.lifecycle.ViewModel;

import com.cs446.group18.timetracker.entity.Geolocation;
import com.cs446.group18.timetracker.repository.GeolocationRepository;

import java.util.List;

public class GeolocationViewModel extends ViewModel {
    private GeolocationRepository repository;

    public GeolocationViewModel(GeolocationRepository repository) {
        this.repository = repository;
    }

    public List<Geolocation> getGeolocations() {
        return repository.getGeolocations();
    }

    public Geolocation getGeolocationById(long id) {
        return repository.getGeolocationById(id);
    }

    public void insert(Geolocation geolocation) {
        repository.createGeolocation(geolocation);
    }
}

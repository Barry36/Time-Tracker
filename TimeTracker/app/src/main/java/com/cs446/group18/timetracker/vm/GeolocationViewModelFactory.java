package com.cs446.group18.timetracker.vm;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.cs446.group18.timetracker.repository.GeolocationRepository;

public class GeolocationViewModelFactory extends ViewModelProvider.NewInstanceFactory {
    private GeolocationRepository geolocationRepository;

    public GeolocationViewModelFactory(GeolocationRepository geolocationRepository) {
        this.geolocationRepository = geolocationRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        GeolocationViewModel geolocationViewModel = new GeolocationViewModel(geolocationRepository);
        return (T) geolocationViewModel;
    }
}

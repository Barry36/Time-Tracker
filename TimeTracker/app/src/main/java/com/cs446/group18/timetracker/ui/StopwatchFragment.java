package com.cs446.group18.timetracker.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;


import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.cs446.group18.timetracker.R;

import com.cs446.group18.timetracker.entity.Geolocation;
import com.cs446.group18.timetracker.entity.TimeEntry;
import com.cs446.group18.timetracker.utils.InjectorUtils;
import com.cs446.group18.timetracker.vm.GeolocationViewModel;
import com.cs446.group18.timetracker.vm.GeolocationViewModelFactory;
import com.cs446.group18.timetracker.vm.TimeEntryListViewModelFactory;
import com.cs446.group18.timetracker.vm.TimeEntryViewModel;

import java.math.BigDecimal;
import java.math.RoundingMode;

import java.util.Date;

public class StopwatchFragment extends Fragment{
    public boolean stopBtnClicked;
    private long mTimerSoFarInMillis;
    private boolean mTimerRunning;
    private long pauseOffset = 0;
    private Date startTime;
    private Date endTime;


    public static StopwatchFragment newInstance(long eventId) {
        StopwatchFragment stopwatchFragment = new StopwatchFragment();
        Bundle args = new Bundle();
        args.putLong("id", eventId);
        stopwatchFragment.setArguments(args);
        return stopwatchFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        TimeEntryListViewModelFactory timeEntryListViewModelFactory = InjectorUtils.provideTimeEntryListViewModelFactory(getActivity());
        TimeEntryViewModel timeEntryViewModel = new ViewModelProvider(this, timeEntryListViewModelFactory).get(TimeEntryViewModel.class);
        GeolocationViewModelFactory geolocationViewModelFactory = InjectorUtils.provideGeolocationViewModelFactory(getActivity());
        GeolocationViewModel geolocationViewModel = new ViewModelProvider(this, geolocationViewModelFactory).get(GeolocationViewModel.class);


        Long eventId = getArguments().getLong("id");
        Log.w("Id", eventId.toString());

        final View rootView = inflater.inflate(R.layout.fragment_stopwatch, container, false);
        final Chronometer chronometer = rootView.findViewById(R.id.chronometer);
        
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if ((SystemClock.elapsedRealtime() - chronometer.getBase()) >= 60000) {
                    String test = Long.toString(SystemClock.elapsedRealtime() - chronometer.getBase());
                    Log.w("Log time entry", test);
                }
            }
        });

        final ImageView mButtonStartPause = rootView.findViewById(R.id.button_start_pause);
        mButtonStartPause.setOnClickListener(v -> {

            if (!mTimerRunning) {
//                mButtonStartPause.setText("Pause");
                startTime = new Date();
                chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
                chronometer.start();
                mTimerRunning = true;
                mButtonStartPause.setVisibility(View.GONE);
            } else {
//                mButtonStartPause.setText("Start");
                chronometer.stop();
                pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
                mTimerRunning = false;
            }
        });

        final ImageView mButtonStop = rootView.findViewById(R.id.button_stop);
        mButtonStop.setOnClickListener(v -> {
            if (mTimerRunning) {
                //if the stopwatch has not yet been paused
                if (pauseOffset > 0) {
                    mTimerSoFarInMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
                } else {
                    mTimerSoFarInMillis = SystemClock.elapsedRealtime() - pauseOffset - chronometer.getBase();
                    Log.w("pauseoffset is not larger than 0", Long.toString(mTimerSoFarInMillis));
                }
                Log.w("stopped when it is running", Long.toString(mTimerSoFarInMillis));
            } else {
                //if the stopwatch is paused
                mTimerSoFarInMillis = pauseOffset;
                Log.w("stopped when it is paused", Long.toString(mTimerSoFarInMillis));
            }
            mTimerRunning = false;
            chronometer.stop();
            endTime = new Date();
            long timeEntryId = timeEntryViewModel.insert(new TimeEntry(eventId, startTime, endTime, mTimerSoFarInMillis));
            LocationManager lm = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            @SuppressLint("MissingPermission")
            Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            double longitude = round(location.getLongitude(), 4);
            double latitude = round(location.getLatitude(), 4);
            geolocationViewModel.insert(new Geolocation(timeEntryId, latitude, longitude));
//            mButtonStartPause.setText("Start");
            pauseOffset = 0;
            chronometer.setBase(SystemClock.elapsedRealtime());
            mButtonStartPause.setVisibility(View.VISIBLE);
        });
        return rootView;
    }

    public boolean getHiddenBtnValue() {
        return stopBtnClicked;
    }

    private double round(double value, int places) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

}

package com.cs446.group18.timetracker.ui;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.cs446.group18.timetracker.R;

public class StopwatchFragment extends Fragment {
    private long mTimerSoFarInMillis;
    private boolean mTimerRunning;
    private long pauseOffset;
    public StopwatchFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                                ViewGroup container, Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_stopwatch, container, false);
        final Chronometer chronometer = rootView.findViewById(R.id.chronometer);
        // when the app got restored calculate the new time and set base
        chronometer.setBase(SystemClock.elapsedRealtime());
        mTimerSoFarInMillis = SystemClock.elapsedRealtime();
        chronometer.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
            @Override
            public void onChronometerTick(Chronometer chronometer) {
                if ((SystemClock.elapsedRealtime() - chronometer.getBase()) >= 60000) {
                    // pass a time entry back to store to database to avoid getting shut down
                    String test = Long.toString(SystemClock.elapsedRealtime() - chronometer.getBase());
                    Log.w("Log time entry", test);
                }
            }
        });

        final Button mButtonStartPause = rootView.findViewById(R.id.button_start_pause);
        mButtonStartPause.setOnClickListener(v -> {
            if(!mTimerRunning) {
                Log.w("what is this", "running");
                chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
                chronometer.start();
                mTimerRunning = true;
            }else{
                Log.w("what is this", "stopped");
                chronometer.stop();
                pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
                mTimerRunning = false;
            }
        });
        return rootView;
    }

}

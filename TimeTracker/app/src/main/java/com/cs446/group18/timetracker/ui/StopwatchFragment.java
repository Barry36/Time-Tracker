package com.cs446.group18.timetracker.ui;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;

import androidx.fragment.app.Fragment;

import com.cs446.group18.timetracker.R;

public class StopwatchFragment extends Fragment {
    private long mTimerSoFarInMillis;
    private boolean mTimerRunning;
    private long pauseOffset = 0;
    public StopwatchFragment() {

    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                                ViewGroup container, Bundle savedInstanceState) {
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

        final Button mButtonStartPause = rootView.findViewById(R.id.button_start_pause);
        mButtonStartPause.setOnClickListener(v -> {
            if(!mTimerRunning) {
                mButtonStartPause.setText("Pause");
                chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
                chronometer.start();
                mTimerRunning = true;
            }else{
                mButtonStartPause.setText("Start");
                chronometer.stop();
                pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
                mTimerRunning = false;
            }
        });
        final Button mButtonStop = rootView.findViewById(R.id.button_stop);
        mButtonStop.setOnClickListener(v -> {
            if(mTimerRunning) {
                //if the stopwatch has not yet been paused
                if(pauseOffset > 0) {
                    mTimerSoFarInMillis = SystemClock.elapsedRealtime() - chronometer.getBase();
                }else{
                    mTimerSoFarInMillis = SystemClock.elapsedRealtime() - pauseOffset - chronometer.getBase();
                    Log.w("pauseoffset is not larger than 0", Long.toString(mTimerSoFarInMillis));
                }
                Log.w("stopped when it is running", Long.toString(mTimerSoFarInMillis));
            }else{
                //if the stopwatch is paused
                mTimerSoFarInMillis = pauseOffset;
                Log.w("stopped when it is paused", Long.toString(mTimerSoFarInMillis));
            }
            mTimerRunning = false;
            chronometer.stop();
            chronometer.setBase(SystemClock.elapsedRealtime());
            pauseOffset = 0;
            mButtonStartPause.setText("Start");
            //TODO: send back the time

        });
        return rootView;
    }

}

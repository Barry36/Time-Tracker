package com.cs446.group18.timetracker.ui;

import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.cs446.group18.timetracker.R;
import com.cs446.group18.timetracker.entity.TimeEntry;
import com.cs446.group18.timetracker.utils.InjectorUtils;
import com.cs446.group18.timetracker.vm.TimeEntryListViewModelFactory;
import com.cs446.group18.timetracker.vm.TimeEntryViewModel;

import java.util.Date;

public class StopwatchFragment extends Fragment {
    public boolean stopBtnClicked;
    private long mTimerSoFarInMillis;
    private boolean mTimerRunning;
    private long pauseOffset = 0;
    private Date startTime;
    private Date endTime;
    public static StopwatchFragment newInstance(long eventId) {
        StopwatchFragment stopwatchFragment= new StopwatchFragment();
        Bundle args = new Bundle();
        args.putLong("id", eventId);
        stopwatchFragment.setArguments(args);
        return stopwatchFragment;
    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                                ViewGroup container, Bundle savedInstanceState) {
        TimeEntryListViewModelFactory factory = InjectorUtils.provideTimeEntryListViewModelFactory(getActivity());
        TimeEntryViewModel viewModel = new ViewModelProvider(this, factory).get(TimeEntryViewModel.class);

        Long eventId = getArguments().getLong("id");
        Log.w("Id", eventId.toString());

        final View rootView = inflater.inflate(R.layout.fragment_stopwatch, container, false);
        final Chronometer chronometer = rootView.findViewById(R.id.chronometer);
        startTime = new Date();
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

            // Set Flag
            TextView textView = rootView.findViewById(R.id.btn_clicked);
            textView.setText("0");
            this.stopBtnClicked = false;
            // Set Flag Ends

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
            // Set Flag
            TextView timer_stop_clicked = rootView.findViewById(R.id.btn_clicked);
            timer_stop_clicked.setText("1");
            this.stopBtnClicked = true;

            // Set Flag Ends
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
            endTime = new Date();
            viewModel.insert(new TimeEntry(eventId, startTime, endTime, mTimerSoFarInMillis));

            mButtonStartPause.setText("Start");
            pauseOffset = 0;
            chronometer.setBase(SystemClock.elapsedRealtime());

        });
        return rootView;
    }

    public boolean getHiddenBtnValue(){
        return stopBtnClicked;
    }

}

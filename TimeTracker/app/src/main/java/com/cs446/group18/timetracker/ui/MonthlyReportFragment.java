package com.cs446.group18.timetracker.ui;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.cs446.group18.timetracker.R;
import com.cs446.group18.timetracker.entity.TimeEntry;
import com.cs446.group18.timetracker.persistence.TimeTrackerDatabase;
import com.cs446.group18.timetracker.relation.EventWithTimeEntries;
import com.cs446.group18.timetracker.repository.TimeEntryRepository;
import com.cs446.group18.timetracker.utils.ReportUtil;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.PieData;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

public class MonthlyReportFragment extends Fragment {
    private String[] XLabels = {"Week1", "Week2", "Week3", "Week4"};
    private ArrayList<String> events;
    private ArrayList<String> labels;
    private ArrayList<Float> pieData;
    private ArrayList<Float> barDataAll;
    private ArrayList<ArrayList<Float>> barDataOne;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_monthly_report, container, false);
        PieChart pieChart = v.findViewById(R.id.m_pie_chart);
        BarChart barChartAll = v.findViewById(R.id.m_bar_chart_all);
        BarChart barChartOne = v.findViewById(R.id.m_bar_chart_one);

        events = new ArrayList<>();
        labels = new ArrayList<>();
        pieData = new ArrayList<>();
        barDataAll = new ArrayList<>();
        barDataOne = new ArrayList<>();

        class MyAsyncTask extends AsyncTask<Void, Void, Boolean> {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Boolean doInBackground(Void... v) {
                TimeEntryRepository timeEntryRepository = TimeEntryRepository.getInstance(
                        TimeTrackerDatabase.getInstance(getContext()).timeEntryDao());
                List<EventWithTimeEntries> eventsWithTimeEntries = timeEntryRepository.getEventsWithTimeEntriesStatic();

                updateData(eventsWithTimeEntries);
                return true;
            }

            @Override
            protected void onPostExecute(Boolean success) {
                PieData dataP = ReportUtil.generatePieData(labels, pieData);
                ReportUtil.drawPieChart(pieChart, dataP);

                BarData dataB = ReportUtil.generateBarData(barDataAll);
                ReportUtil.drawBarChart(barChartAll, dataB, labels);

                ArrayList<String> xValsOne = new ArrayList<>();
                Collections.addAll(xValsOne, XLabels);
                Spinner spinner_event = v.findViewById(R.id.m_spinner_event);
                ArrayAdapter adapter_event = new ArrayAdapter(spinner_event.getContext(),
                        android.R.layout.simple_spinner_item, events);
                adapter_event.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_event.setAdapter(adapter_event);
                spinner_event.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        BarData barData = ReportUtil.generateBarData(barDataOne.get(position));
                        ReportUtil.drawBarChart(barChartOne, barData, xValsOne);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
            }
        }
        new MyAsyncTask().execute();

        return v;
    }

    private void updateData(List<EventWithTimeEntries> eventsWithTimeEntries) {
        Calendar c = Calendar.getInstance();
        float totalTime = 0;

        for (int i = 0; i < eventsWithTimeEntries.size(); i++) {
            EventWithTimeEntries event = eventsWithTimeEntries.get(i);
            ArrayList<Float> yVals = new ArrayList<>();
            for (int j = 0; j < XLabels.length; j++) {
                yVals.add(0f);
            }
            float timeSpend = 0;
            List<TimeEntry> entries = event.getCurrMonthTimeEntries();
            if (!entries.isEmpty()) {
                String eventName = event.getEvent().getEventName();
                events.add(eventName);
                labels.add(eventName);
                for (int j = 0; j < entries.size(); j++) {
                    TimeEntry e = entries.get(j);
                    c.setTime(e.getStartTime());
                    int index = c.get(Calendar.WEEK_OF_MONTH) - 1;
                    yVals.set(index,
                            yVals.get(index) + ReportUtil.MillisToHours((float) e.getDuration()));
                    timeSpend += ReportUtil.MillisToHours((float) e.getDuration());
                }
                barDataAll.add(timeSpend);
                barDataOne.add(yVals);
            }
            totalTime += timeSpend;
        }

        float other_time = 0;
        int i = 0;
        while (i < barDataAll.size()) {
            float p = barDataAll.get(i) / totalTime * 100;
            if (p < 3) {
                other_time += barDataAll.get(i);
                barDataAll.remove(i);
                labels.remove(i);
            } else {
                pieData.add(p);
                i++;
            }
        }
        if (other_time > 0) {
            labels.add("Other");
            barDataAll.add(other_time);
            pieData.add(other_time / totalTime * 100);
        }
    }
}

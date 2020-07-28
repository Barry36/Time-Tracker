package com.cs446.group18.timetracker.ui;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.fragment.app.Fragment;

import com.cs446.group18.timetracker.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;
import java.util.Collections;

public class WeeklyReportFragment extends Fragment {

    protected Typeface tf = Typeface.SERIF;
    protected float textSize = 11f;
    public static final int[] LIGHT14 = {
            Color.rgb(192, 255, 140), Color.rgb(255, 247, 140),
            Color.rgb(255, 208, 140), Color.rgb(140, 234, 255),
            Color.rgb(255, 140, 157), Color.rgb(174, 199, 232),
            Color.rgb(197, 176, 213), Color.rgb(196, 156, 73),
            Color.rgb(247, 182, 210), Color.rgb(199, 199, 199),
            Color.rgb(219, 219, 141), Color.rgb(158, 218, 229),
            Color.rgb(255, 187, 120), Color.rgb(152, 223, 138)
    };

    private String[] XLabels = {"Mon.", "Tue.", "Wed.", "Thu.", "Fri.",
            "Sat.", "Sun."};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_weekly_report, container, false);

        PieChart pieChart = v.findViewById(R.id.w_pie_chart);
        BarChart barChartAll = v.findViewById(R.id.w_bar_chart_all);
        BarChart barChartOne = v.findViewById(R.id.w_bar_chart_one);

        // Pie Chart
        ArrayList<Pair<String, Float>> testP = new ArrayList<>();
        testP.add(new Pair<>("Event 1", 42.8f));
        testP.add(new Pair<>("Event 2", 19.6f));
        testP.add(new Pair<>("Event 3", 31.2f));
        testP.add(new Pair<>("Other", 100 - 42.8f - 31.2f - 19.6f));
        PieData pieData = generatePieData(testP);
        drawPieChart(pieChart, pieData);

        // Bar Chart All
        ArrayList<String> xValsAll = new ArrayList<>();
        xValsAll.add("Event 1");
        xValsAll.add("Event 2");
        xValsAll.add("Event 3");
        xValsAll.add("Other");
        ArrayList<Float> yValsAll = new ArrayList<>();
        yValsAll.add(42.8f / 100 * 40);
        yValsAll.add(19.6f / 100 * 40);
        yValsAll.add(31.2f / 100 * 40);
        yValsAll.add((100 - 42.8f - 31.2f - 19.6f) /100 * 40);
        BarData barDataAll = generateBarDataAll(yValsAll);
        drawBarChart(barChartAll, barDataAll, xValsAll);

        // Bar Chart One
        ArrayList<String> xValsOne = new ArrayList<>();
        Collections.addAll(xValsOne, XLabels);
        ArrayList<ArrayList<Float>> yValsSet = new ArrayList<>();
        ArrayList<Float> yValsOne1 = new ArrayList<>();
        for (int i = 0; i < xValsOne.size(); i++) {
            yValsOne1.add((i + 1) * 40.8f / 100 * 10);
        }
        yValsSet.add(yValsOne1);
        ArrayList<Float> yValsOne2 = new ArrayList<>();
        for (int i = 0; i < xValsOne.size(); i++) {
            yValsOne2.add((10-i) * 40.8f / 100 * 10);
        }
        yValsSet.add(yValsOne2);
        yValsSet.add(yValsOne1);
        yValsSet.add(yValsOne1);

        Spinner spinner_event = v.findViewById(R.id.w_spinner_event);
        ArrayAdapter<CharSequence> adapter_event = ArrayAdapter.createFromResource(getContext(),
                R.array.events, android.R.layout.simple_spinner_item);
        adapter_event.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_event.setAdapter(adapter_event);
        spinner_event.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                BarData barData = generateBarDataAll(yValsSet.get(position));
                drawBarChart(barChartOne, barData, xValsOne);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return v;
    }

    private void drawPieChart(PieChart chart, PieData data) {
        chart.getDescription().setEnabled(false);
        chart.setCenterText("Time Spending");
        chart.setCenterTextSize(11f);
        chart.setCenterTextTypeface(tf);

        chart.setHoleRadius(40f);
        chart.setTransparentCircleRadius(47f);

        chart.setUsePercentValues(true);
        chart.setExtraOffsets(5, 5, 40, 5);

        data.setValueFormatter(new PercentFormatter(chart));
        data.setValueTypeface(tf);
        data.setValueTextSize(textSize);
        data.setValueTextColor(Color.BLACK);
        chart.setEntryLabelTypeface(tf);
        chart.setEntryLabelTextSize(textSize);
        chart.setEntryLabelColor(Color.BLACK);
        chart.setData(data);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        chart.animateY(800);
        chart.invalidate();
    }

    private void drawBarChart(BarChart chart, BarData data, ArrayList<String> xVals) {
        chart.getDescription().setEnabled(false);
        chart.setDrawGridBackground(false);
        chart.setDrawBarShadow(false);

        XAxis xAxis = chart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTypeface(tf);
        xAxis.setTextSize(textSize);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(true);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(xVals));
        xAxis.setLabelCount(xVals.size(),false);

        YAxis leftAxis = chart.getAxisLeft();
        leftAxis.setTypeface(tf);
        leftAxis.setLabelCount(5, false);
        leftAxis.setSpaceTop(20f);
        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        YAxis rightAxis = chart.getAxisRight();
        rightAxis.setTypeface(tf);
        rightAxis.setLabelCount(5, false);
        rightAxis.setSpaceTop(20f);
        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)

        data.setValueTypeface(tf);
        data.setValueTextSize(textSize);

        chart.setData(data);
        chart.setFitBars(true);

        chart.animateY(800);
        chart.invalidate();
    }

//    private void drawBarChartOne(BarChart chart, BarData data) {
//        chart.getDescription().setEnabled(false);
//        chart.setDrawGridBackground(false);
//        chart.setDrawBarShadow(false);
//
//        XAxis xAxis = chart.getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        xAxis.setTypeface(tf);
//        xAxis.setDrawGridLines(false);
//        xAxis.setDrawAxisLine(true);
//
//        YAxis leftAxis = chart.getAxisLeft();
//        leftAxis.setTypeface(tf);
//        leftAxis.setLabelCount(5, false);
//        leftAxis.setSpaceTop(20f);
//        leftAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//
//        YAxis rightAxis = chart.getAxisRight();
//        rightAxis.setTypeface(tf);
//        rightAxis.setLabelCount(5, false);
//        rightAxis.setSpaceTop(20f);
//        rightAxis.setAxisMinimum(0f); // this replaces setStartAtZero(true)
//
//        data.setValueTypeface(tf);
//
//        chart.setData(data);
//        chart.setFitBars(true);
//
//        chart.animateY(800);
//        chart.invalidate();
//    }

    private PieData generatePieData(ArrayList<Pair<String, Float>> data) {

        ArrayList<PieEntry> entries = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            entries.add(new PieEntry(data.get(i).second, data.get(i).first));
        }

        PieDataSet d = new PieDataSet(entries, "");

        d.setColors(LIGHT14);
        d.setSliceSpace(2f);

        return new PieData(d);
    }

    private BarData generateBarDataAll(ArrayList<Float> data) {
        ArrayList<BarEntry> entries = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            entries.add(new BarEntry(i, data.get(i)));
        }

        BarDataSet d = new BarDataSet(entries, "Time Spending (in hours)");
        d.setColors(LIGHT14);
        d.setHighLightAlpha(255);

        BarData cd = new BarData(d);
        cd.setBarWidth(0.8f); // 1f - without space between columns; more space, decrease
        return cd;
    }
}
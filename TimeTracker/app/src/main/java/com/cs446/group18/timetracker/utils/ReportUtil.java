package com.cs446.group18.timetracker.utils;

import android.graphics.Color;
import android.graphics.Typeface;

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

public class ReportUtil {
    public static Typeface tf = Typeface.SERIF;
    public static float textSize = 11f;
    public static final int[] LIGHT14 = {
            Color.rgb(192, 255, 140), Color.rgb(255, 247, 140),
            Color.rgb(255, 208, 140), Color.rgb(140, 234, 255),
            Color.rgb(255, 140, 157), Color.rgb(174, 199, 232),
            Color.rgb(197, 176, 213), Color.rgb(196, 156, 73),
            Color.rgb(247, 182, 210), Color.rgb(199, 199, 199),
            Color.rgb(219, 219, 141), Color.rgb(158, 218, 229),
            Color.rgb(255, 187, 120), Color.rgb(152, 223, 138)
    };

    public static PieData generatePieData(ArrayList<String> eventNames, ArrayList<Float> data) {

        ArrayList<PieEntry> entries = new ArrayList<>();

        for (int i = 0; i < data.size(); i++) {
            entries.add(new PieEntry(data.get(i), eventNames.get(i)));
        }

        PieDataSet d = new PieDataSet(entries, "");

        d.setColors(LIGHT14);
        d.setSliceSpace(1f);

        return new PieData(d);
    }

    public static void drawPieChart(PieChart chart, PieData data) {
        chart.getDescription().setEnabled(false);
        chart.setCenterText("Time Spending");
        chart.setCenterTextSize(11f);
        chart.setCenterTextTypeface(tf);

        chart.setHoleRadius(40f);
        chart.setTransparentCircleRadius(47f);

        chart.setUsePercentValues(true);
        chart.setExtraOffsets(5, 5, 5, 0);

        data.setValueFormatter(new PercentFormatter(chart));
        data.setValueTypeface(tf);
        data.setValueTextSize(textSize);
        data.setValueTextColor(Color.BLACK);
        chart.setEntryLabelTypeface(tf);
        chart.setEntryLabelTextSize(textSize);
        chart.setEntryLabelColor(Color.BLACK);
        chart.setData(data);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);

        chart.invalidate();
    }

    public static BarData generateBarData(ArrayList<Float> data) {
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

    public static void drawBarChart(BarChart chart, BarData data, ArrayList<String> xVals) {
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

        chart.invalidate();
    }

    public static float MillisToHours(float d) {
        return d/(1000 * 60 * 60);
    }
}

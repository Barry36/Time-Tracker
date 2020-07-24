package com.cs446.group18.timetracker.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cs446.group18.timetracker.R;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;

import java.util.ArrayList;
import java.util.List;

public class ReportFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_report,  container, false);

        PieChart pieChart = (PieChart) v.findViewById(R.id.pieChart);
        BarChart barChart = (BarChart) v.findViewById(R.id.barChart);

        Spinner spinner_chart_type = v.findViewById(R.id.type_spinner);
        ArrayAdapter<CharSequence> adapter_chart_type = ArrayAdapter.createFromResource(getContext(), R.array.chart_types, android.R.layout.simple_spinner_item);
        adapter_chart_type.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_chart_type.setAdapter(adapter_chart_type);
        spinner_chart_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String chart = parent.getItemAtPosition(position).toString();
                // Pie Chart
                if (position == 0) {
                    barChart.setVisibility(View.GONE);
                    pieChart.setVisibility(View.VISIBLE);
                    drawPieChart(pieChart);
                } else if (position == 1) {
                    pieChart.setVisibility(View.GONE);
                    barChart.setVisibility(View.VISIBLE);
                    drawBarChart(barChart);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        return v;
    }

    private void drawPieChart(PieChart pieChart) {
        List<PieEntry> strings = new ArrayList<>();
        strings.add(new PieEntry(30f,"Event1"));
        strings.add(new PieEntry(70f,"Event2"));

        PieDataSet dataSet = new PieDataSet(strings, "");

        ArrayList<Integer> colors = new ArrayList<Integer>();
        colors.add(Color.RED);
        colors.add(Color.BLUE);
        dataSet.setColors(colors);

        Description description = new Description();
        description.setText("");
        pieChart.setDescription(description);

        pieChart.setHoleRadius(0f);
        pieChart.setTransparentCircleRadius(0f);

        PieData pieData = new PieData(dataSet);
        pieData.setDrawValues(true);

        pieData.setValueFormatter(new PercentFormatter()); //not working currently, check why
        pieData.setValueTextSize(12f);

        pieChart.setData(pieData);
        pieChart.invalidate();
    }

    private void drawBarChart(BarChart barChart) {
        ArrayList<BarEntry> yVals1 = new ArrayList<>();
        ArrayList<BarEntry> yVals2 = new ArrayList<>();
        ArrayList<String> xVals = new ArrayList<>();

        xVals.add("Week1");
        xVals.add("Week2");
        xVals.add("Week3");

        // data
        for (int i = 0; i < 3; i++) {
            yVals1.add(new BarEntry(i, 10 * i + 5));
            yVals2.add(new BarEntry(i,30 * i + 5));
        }

        BarDataSet barDataSet = new BarDataSet(yVals1, "Event1 Time");
        barDataSet.setColor(Color.RED);

        BarDataSet barDataSet2 = new BarDataSet(yVals2, "Event2 Time");
        barDataSet2.setColor(Color.BLUE);

        List<IBarDataSet> data = new ArrayList<>();
        data.add(barDataSet);
        data.add(barDataSet2);

        BarData bardata = new BarData(data);

        // (barWidth + barSpace) * barAmount + groupSpace = 1.00
        int barAmount = 2;
        float groupSpace = 0.3f;
        float barWidth = (1f - groupSpace) / barAmount;
        float barSpace = 0f;

        bardata.setBarWidth(barWidth);
        bardata.groupBars(0f, groupSpace, barSpace);

        barChart.setData(bardata);
        barChart.getLegend().setForm(Legend.LegendForm.CIRCLE);

        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setAxisMaximum(3);

        barChart.getAxisRight().setEnabled(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisLeft().setAxisMinimum(0.0f);

        Description description = new Description();
        description.setText("");
        barChart.setDescription(description);

        barChart.animateXY(1000, 2000);
        barChart.invalidate();
    }
}
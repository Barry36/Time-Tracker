package com.cs446.group18.timetracker.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.cs446.group18.timetracker.ui.MonthlyReportFragment;
import com.cs446.group18.timetracker.ui.WeeklyReportFragment;
import com.cs446.group18.timetracker.ui.YearlyReportFragment;

public class PageAdapter extends FragmentPagerAdapter {

    private int numOfTabs;
    private Fragment weeklyReport;
    private Fragment monthlyReport;
    private Fragment yearlyReport;

    public PageAdapter(FragmentManager fm, int numOfTabs) {
        super(fm);
        this.numOfTabs = numOfTabs;
        weeklyReport = new WeeklyReportFragment();
        monthlyReport = new MonthlyReportFragment();
        yearlyReport = new YearlyReportFragment();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return weeklyReport;
            case 1:
                return monthlyReport;
            case 2:
                return yearlyReport;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numOfTabs;
    }
}

package com.cs446.group18.timetracker.persistence;

import android.app.Application;
import android.content.Context;

public class LoadJSON extends Application {
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }
}

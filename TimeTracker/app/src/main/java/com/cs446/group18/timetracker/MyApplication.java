package com.cs446.group18.timetracker;

import android.app.Application;
import android.content.Context;

public class MyApplication extends Application {

private static Context context;

public static Context getContext() {
return context;
}

public static void setContext(Context context) {
MyApplication.context = context;
}

@Override
public void onCreate() {
super.onCreate();

setContext(this);
}
}
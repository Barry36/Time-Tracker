package com.cs446.group18.timetracker.service;

import android.content.res.Resources;

public interface NotificationStrategy {
    void notify(Resources resources, String[] messages);
}

package com.cs446.group18.timetracker.service;

import android.content.res.Resources;

public class NotificationStrategyContext {
    private NotificationStrategy strategy;

    public NotificationStrategyContext(NotificationStrategy strategy) {
        this.strategy = strategy;
    }

    public void notify(Resources resources, String[] messages) {
        this.strategy.notify(resources, messages);
    }
}

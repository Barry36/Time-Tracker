package com.cs446.group18.timetracker.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import com.cs446.group18.timetracker.R;
import com.cs446.group18.timetracker.constants.NotificationConstant;
import com.cs446.group18.timetracker.ui.MainActivity;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class ForecastingNotificationStrategy implements NotificationStrategy {
    private Context context;
    private NotificationManagerCompat notificationManager;

    public ForecastingNotificationStrategy(Context context) {
        this.context = context;
        this.notificationManager = NotificationManagerCompat.from(context);
    }

    @Override
    public void notify(Resources resources, String[] messages) {
        Intent activityIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(
                context,
                0,
                activityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        Notification notification = new NotificationCompat.Builder(context, NotificationConstant.FORECASTING_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .setSummaryText("Forecasting service"))
                .setContentTitle("Suggested schedule for Study: " + messages[0])
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.rgb(15, 163, 232))
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .build();
        notificationManager.notify(3, notification);
    }
}

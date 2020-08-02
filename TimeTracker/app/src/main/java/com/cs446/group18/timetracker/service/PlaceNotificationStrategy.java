package com.cs446.group18.timetracker.service;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.cs446.group18.timetracker.R;
import com.cs446.group18.timetracker.constants.NotificationConstant;
import com.cs446.group18.timetracker.ui.MainActivity;

public class PlaceNotificationStrategy implements NotificationStrategy {
    private Context context;
    private NotificationManagerCompat notificationManager;

    public PlaceNotificationStrategy(Context context) {
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

        Bitmap largeIcon = BitmapFactory.decodeResource(resources, R.drawable.checklist);

        Notification notification = new NotificationCompat.Builder(context, NotificationConstant.GEOLOCATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_notification)
                .setLargeIcon(largeIcon)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .setSummaryText("place service"))
                .setContentTitle("You are close to: " + messages[0])
                .setContentText("You can start to track for event: " + messages[1])
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                .setColor(Color.rgb(15, 163, 232))
                .setContentIntent(contentIntent)
                .setAutoCancel(true)
                .setOnlyAlertOnce(true)
                .build();
        notificationManager.notify(2, notification);
    }
}

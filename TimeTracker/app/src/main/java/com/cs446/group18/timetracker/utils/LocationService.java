package com.cs446.group18.timetracker.utils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.cs446.group18.timetracker.BuildConfig;
import com.cs446.group18.timetracker.R;
import com.cs446.group18.timetracker.constants.LocationConstant;
import com.cs446.group18.timetracker.constants.NotificationConstant;
import com.cs446.group18.timetracker.constants.QuadTreeConstant;
import com.cs446.group18.timetracker.entity.Event;
import com.cs446.group18.timetracker.entity.Geolocation;
import com.cs446.group18.timetracker.entity.TimeEntry;
import com.cs446.group18.timetracker.model.Neighbour;
import com.cs446.group18.timetracker.model.QuadTree;
import com.cs446.group18.timetracker.persistence.TimeTrackerDatabase;
import com.cs446.group18.timetracker.repository.EventRepository;
import com.cs446.group18.timetracker.repository.GeolocationRepository;
import com.cs446.group18.timetracker.repository.TimeEntryRepository;
import com.cs446.group18.timetracker.ui.MainActivity;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class LocationService extends Service {
    private LocationCallback mLocationCallback;
    private QuadTree quadTree;
    private GeolocationRepository geolocationRepository;
    private TimeEntryRepository timeEntryRepository;
    private EventRepository eventRepository;
    private NotificationManagerCompat notificationManager;
    private Context context;

    public LocationService() {
        this.quadTree = new QuadTree();
        this.geolocationRepository = GeolocationRepository.getInstance(TimeTrackerDatabase.getInstance(this).geolocationDao());
        this.timeEntryRepository = TimeEntryRepository.getInstance(TimeTrackerDatabase.getInstance(this).timeEntryDao());
        this.eventRepository = EventRepository.getInstance(TimeTrackerDatabase.getInstance(this).eventDao());
    }

    @Override
    public void onCreate() {
        super.onCreate();
        this.context = getApplicationContext();
    }

    private class GetAddress extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... strings) {
            try {
                double lat = Double.parseDouble(strings[0].split(",")[0]);
                double lng = Double.parseDouble(strings[0].split(",")[1]);
                String response;
                HttpRequestHandler requestHandler = new HttpRequestHandler();
                String API_KEY = BuildConfig.API_KEY;
                String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?latlng=%.4f,%.4f&key=%s", lat, lng, API_KEY);
                response = requestHandler.getResponse(url);
                return response;
            } catch (Exception ex) {
                Log.e("Http Error", ex.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONObject obj = ((JSONArray) jsonObject.get("results")).getJSONObject(0);
                String address = obj.get("formatted_address").toString();
                String inputTypes = ((JSONArray) jsonObject.get("results")).getJSONObject(0).get("types").toString();
                String[] placeTypes = inputTypes.substring(1, inputTypes.length() - 1).replaceAll("\"", "").split(",");

                // TODO: check for event place mapping here
                if (!placeTypes[0].equals("street_address")) {
                    Intent activityIntent = new Intent(getApplicationContext(), MainActivity.class);
                    PendingIntent contentIntent = PendingIntent.getActivity(
                            getApplicationContext(),
                            0,
                            activityIntent,
                            PendingIntent.FLAG_UPDATE_CURRENT
                    );
                    Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.checklist);

                    Notification notification = new NotificationCompat.Builder(getApplicationContext(), NotificationConstant.GEOLOCATION_CHANNEL_ID)
                            .setSmallIcon(R.drawable.ic_notification)
                            .setLargeIcon(largeIcon)
                            .setStyle(new NotificationCompat.BigTextStyle()
                                    .setSummaryText("place service"))
                            .setContentText("You are now in the range of: " + Arrays.toString(placeTypes))
                            .setPriority(NotificationCompat.PRIORITY_HIGH)
                            .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                            .setColor(Color.rgb(15, 163, 232))
                            .setContentIntent(contentIntent)
                            .setAutoCancel(true)
                            .setOnlyAlertOnce(true)
                            .build();
                    notificationManager.notify(2, notification);
                }

                Log.d("Current address", address);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

//    public LocationCallback locationCallback = new LocationCallback() {
//        @Override
//        public void onLocationResult(LocationResult locationResult) {
//            super.onLocationResult(locationResult);
//            if (locationResult != null && locationResult.getLastLocation() != null) {
//                // get current location latitude and longitude
//                latitude = locationResult.getLastLocation().getLatitude();
//                longitude = locationResult.getLastLocation().getLongitude();
//                Log.d("Current Location", "latitude " + latitude + ", longitude " + longitude);
//            }
//        }
//    };

    private void createLocationCallback() {
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult != null && locationResult.getLastLocation() != null) {
                    // get current location latitude and longitude
                    double latitude = locationResult.getLastLocation().getLatitude();
                    double longitude = locationResult.getLastLocation().getLongitude();
                    new GetAddress().execute(String.format("%.4f,%.4f", latitude, longitude));

                    Log.d("Current geolocation", "latitude " + latitude + ", longitude " + longitude);
                    Set<Neighbour> neighbours = quadTree.findNeighbours(latitude, longitude, QuadTreeConstant.QUADTREE_LAST_NODE_SIZE_IN_KM);
                    Map<Long, Integer> freq = new HashMap<>();

                    new Thread(() -> {
                        Event eventToNotify = null;
                        int max = 0;
                        for (Neighbour neighbour : neighbours) {
                            Geolocation geolocation = geolocationRepository.getGeolocationById(neighbour.getId());
                            TimeEntry timeEntry = timeEntryRepository.getTimeEntryById(geolocation.getTimeEntryId());
                            Event event = eventRepository.getEventById(timeEntry.getEventId());
                            int count = freq.getOrDefault(event.getEventId(), 0) + 1;
                            if (max < count) {
                                max = count;
                                eventToNotify = event;
                            }
                            freq.put(event.getEventId(), count);
                            Log.d("Adjacent neighbor detected", neighbour.toString());
                        }
                        if (eventToNotify != null) {
                            Intent activityIntent = new Intent(getApplicationContext(), MainActivity.class);
                            PendingIntent contentIntent = PendingIntent.getActivity(
                                    getApplicationContext(),
                                    0,
                                    activityIntent,
                                    PendingIntent.FLAG_UPDATE_CURRENT
                            );
                            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.checklist);

                            Notification notification = new NotificationCompat.Builder(getApplicationContext(), NotificationConstant.GEOLOCATION_CHANNEL_ID)
                                    .setSmallIcon(R.drawable.ic_notification)
                                    .setLargeIcon(largeIcon)
                                    .setStyle(new NotificationCompat.BigTextStyle()
                                            .setBigContentTitle("Time tracker")
                                            .setSummaryText("geolocation service"))
                                    .setContentText("Track for event: " + eventToNotify.getEventName())
                                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                                    .setCategory(NotificationCompat.CATEGORY_MESSAGE)
                                    .setColor(Color.rgb(15, 163, 232))
                                    .setContentIntent(contentIntent)
                                    .setAutoCancel(true)
                                    .setOnlyAlertOnce(true)
                                    .build();
                            notificationManager.notify(1, notification);
                            Log.d("Event to be notified", eventToNotify.getEventName());
                        }
                    }).start();
                }
            }
        };
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException();
    }

    @SuppressLint("MissingPermission")
    private void startLocationService() {
        // add all geolocation to the quad tree
        new Thread(() -> {
            List<Geolocation> geolocations = geolocationRepository.getGeolocations();
            for (Geolocation geolocation : geolocations) {
                quadTree.addNeighbour(geolocation.getGeolocationId(), geolocation.getLatitude(), geolocation.getLongitude());
            }
        }).start();

        LocationRequest locationRequest = new LocationRequest();
        // get current location every 30 seconds
        locationRequest.setInterval(30000);
//        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // create notificationManager
        this.notificationManager = NotificationManagerCompat.from(context);
        createLocationCallback();
        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, mLocationCallback, Looper.getMainLooper());
    }

    private void stopLocationService() {
        LocationServices.getFusedLocationProviderClient(this)
                .removeLocationUpdates(mLocationCallback);
        stopForeground(true);
        stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent != null) {
            String action = intent.getAction();
            if (action != null) {
                if (action.equals(LocationConstant.ACTION_START_LOCATION_SERVICE)) {
                    startLocationService();
                } else if (action.equals(LocationConstant.ACTION_STOP_LOCATION_SERVICE)) {
                    stopLocationService();
                }
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }
}

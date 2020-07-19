package com.cs446.group18.timetracker.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.cs446.group18.timetracker.BuildConfig;
import com.cs446.group18.timetracker.R;
import com.cs446.group18.timetracker.constants.LocationConstant;
import com.cs446.group18.timetracker.ui.MainActivity;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class LocationService extends Service {
    private LocationCallback mLocationCallback;

    private class GetAddress extends AsyncTask<String, Void, String> {

//        ProgressDialog dialog = new ProgressDialog(MainActivity.this);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            dialog.setMessage("Please wait...");
//            dialog.setCanceledOnTouchOutside(false);
//            dialog.show();
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
                Log.d("Current address", address);
            } catch (JSONException e) {
                e.printStackTrace();
            }

//            if (dialog.isShowing())
//                dialog.dismiss();
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
                }
            }
        };
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @SuppressLint("MissingPermission")
    private void startLocationService() {
        String channelId = "location_notification_channel";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent resultIntent = new Intent();
        PendingIntent pendingIntent = PendingIntent.getActivity(
                getApplicationContext(),
                0,
                resultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );
        NotificationCompat.Builder builder = new NotificationCompat.Builder(
                getApplicationContext(),
                channelId
        );
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setContentTitle("Geolocation Service");
        builder.setDefaults(NotificationCompat.DEFAULT_ALL);
        builder.setContentText("Running");
        builder.setContentIntent(pendingIntent);
        builder.setAutoCancel(false);
        builder.setPriority(NotificationCompat.PRIORITY_MAX);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            if (notificationManager != null && notificationManager.getNotificationChannel(channelId) == null) {
                NotificationChannel notificationchannel = new NotificationChannel(channelId,
                        "Geolocation Service",
                        NotificationManager.IMPORTANCE_HIGH
                );
                notificationchannel.setDescription("This channel is used by geolocation service");
                notificationManager.createNotificationChannel(notificationchannel);
            }
        }

        LocationRequest locationRequest = new LocationRequest();
        // get current location every 30 seconds
        locationRequest.setInterval(30000);
//        locationRequest.setFastestInterval(2000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        createLocationCallback();
        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest, mLocationCallback, Looper.getMainLooper());
        startForeground(LocationConstant.LOCATION_SERVICE_ID, builder.build());
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

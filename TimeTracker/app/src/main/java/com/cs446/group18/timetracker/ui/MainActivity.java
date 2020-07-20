package com.cs446.group18.timetracker.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.cs446.group18.timetracker.BuildConfig;
import com.cs446.group18.timetracker.R;
import com.cs446.group18.timetracker.constants.LocationConstant;
import com.cs446.group18.timetracker.databinding.MainActivityBinding;
import com.cs446.group18.timetracker.repository.GeolocationRepository;
import com.cs446.group18.timetracker.utils.HttpRequestHandler;
import com.cs446.group18.timetracker.utils.LocationService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavController navController;

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainActivityBinding binding = DataBindingUtil.setContentView(this, R.layout.main_activity);
        drawerLayout = binding.drawerLayout;

        setSupportActionBar(binding.toolbar);
        navController = Navigation.findNavController(this, R.id.event_nav_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);
        NavigationUI.setupWithNavController(binding.navigationView, navController);
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE_LOCATION_PERMISSION);
        } else {
            startLocationService();
        }

        findViewById(R.id.button_get_location).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                @SuppressLint("MissingPermission")
                Location location = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                double longitude = location.getLongitude();
                double latitude = location.getLatitude();
                Toast.makeText(getApplicationContext(), latitude + ", " + longitude, Toast.LENGTH_SHORT).show();
                new GetAddress().execute(String.format("%.4f,%.4f", latitude, longitude));
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION && grantResults.length > 0) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationService();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, drawerLayout);
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("deprecation")
    private boolean isLocationServiceRunning() {
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (activityManager != null) {
            for (ActivityManager.RunningServiceInfo runningServiceInfo : activityManager.getRunningServices(Integer.MAX_VALUE)) {
                if (LocationService.class.getName().equals(runningServiceInfo.service.getClassName())) {
                    if (runningServiceInfo.foreground) {
                        return true;
                    }
                }
            }
            return false;
        }
        return false;
    }

    private void startLocationService() {
        if (!isLocationServiceRunning()) {
            Intent intent = new Intent(getApplicationContext(), LocationService.class);
            intent.setAction(LocationConstant.ACTION_START_LOCATION_SERVICE);
            startService(intent);
            Toast.makeText(this, "Geolocation service started", Toast.LENGTH_SHORT).show();
        }
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
                String inputTypes = ((JSONArray) jsonObject.get("results")).getJSONObject(0).get("types").toString();
                String[] placeTypes = inputTypes.substring(1, inputTypes.length() - 1).replaceAll("\"", "").split(",");
                Log.d("Current address place types", Arrays.toString(placeTypes));

                Toast.makeText(getApplicationContext(), "Place type is: " + inputTypes, Toast.LENGTH_SHORT).show();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

//    private void stopLocationService() {
//        if (!isLocationServiceRunning()) {
//            Intent intent = new Intent(getApplicationContext(), LocationService.class);
//            intent.setAction(LocationConstant.ACTION_STOP_LOCATION_SERVICE);
//            startService(intent);
//            Toast.makeText(this, "Location service stopped", Toast.LENGTH_SHORT).show();
//        }
//    }
}
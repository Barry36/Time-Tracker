package com.cs446.group18.timetracker.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.app.PendingIntent;
import android.content.Intent;
import android.Manifest;
import android.app.ActivityManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import com.cs446.group18.timetracker.R;
import com.cs446.group18.timetracker.databinding.ActivityMainBinding;
import com.cs446.group18.timetracker.constants.LocationConstant;
import com.cs446.group18.timetracker.constants.NotificationConstant;
import com.cs446.group18.timetracker.service.ForecastingService;
import com.cs446.group18.timetracker.service.LocationService;


public class MainActivity extends AppCompatActivity {
    OnNewIntentListener newIntentListener;
    private DrawerLayout drawerLayout;
    private NavController navController;
    public String TAG = "MainActivity";
    // NFC-related variables
    private NfcAdapter _nfcAdapter;
    private PendingIntent _nfcPendingIntent;
    IntentFilter[] _readTagFilters;

    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createNotificationChannels();

        ActivityMainBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_main);
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

        _nfcAdapter = NfcAdapter.getDefaultAdapter(this);

        if (_nfcAdapter == null) {
            Toast.makeText(this, "Your device does not support NFC. Cannot run this demo.", Toast.LENGTH_LONG).show();
        }

//        checkNfcEnabled();

        _nfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

        IntentFilter ndefDetected = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            ndefDetected.addDataType("application/com.cs446.group18.timetracker");
        } catch (IntentFilter.MalformedMimeTypeException e) {
            throw new RuntimeException("Could not add MIME type.", e);
        }

        _readTagFilters = new IntentFilter[]{ndefDetected};

        // Start Forecasting service
        startForecastingService();

    }

    private void startForecastingService() {

        ForecastingService forecastingService = new ForecastingService(getApplicationContext());
        forecastingService.startForecastingService(1, getApplicationContext());
        Toast.makeText(this, "Forecasting service started", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.getAction() == null) {
            return;
        }

        if (newIntentListener != null)
            newIntentListener.onNewIntent(intent);
        super.onNewIntent(intent);

        if (intent.getAction().equals(NfcAdapter.ACTION_NDEF_DISCOVERED)) {
            NdefMessage[] c = getNdefMessagesFromIntent(intent);
            if (c != null && c.length > 0 && c[0] != null && c[0].getRecords() != null) {
                NdefMessage[] ndefMessages = getNdefMessagesFromIntent(intent);
                if (ndefMessages != null && ndefMessages.length > 0 && ndefMessages[0].getRecords() != null && ndefMessages[0].getRecords().length > 0 && ndefMessages[0].getRecords()[0] != null) {
                    String identifiedEvent = new String(getNdefMessagesFromIntent(intent)[0].getRecords()[0].getPayload()).split(", ")[1].split("=")[1];
                    Toast.makeText(this, "The Received Event:" + identifiedEvent, Toast.LENGTH_SHORT).show();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    if (getSupportFragmentManager().getFragments().get(0) != null &&
                            getSupportFragmentManager().getFragments().get(0).getChildFragmentManager().getFragments() != null)
                        for (Fragment fragment : getSupportFragmentManager().getFragments().get(0).getChildFragmentManager().getFragments()) {
                            if (fragment instanceof EventListFragment) {
                                ((EventListFragment) fragment).receivedNFCTagEvent(identifiedEvent);
                                break;
                            }
                        }

                }
            }

        } else if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            Toast.makeText(this, "This NFC tag has no NDEF data.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();//_nfcAdapter.enableForegroundDispatch(this, _nfcPendingIntent, _readTagFilters, null);
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

    private void createNotificationChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel geolocationChannel = new NotificationChannel(
                    NotificationConstant.GEOLOCATION_CHANNEL_ID,
                    "Geolocation Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );
            geolocationChannel.setDescription("This is geolocation suggestion channel");
            NotificationChannel channel2 = new NotificationChannel(
                    NotificationConstant.PLACE_CHANNEL_ID,
                    "Place Channel",
                    NotificationManager.IMPORTANCE_LOW
            );
            channel2.setDescription("This is place suggestion channel");
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(geolocationChannel);
            manager.createNotificationChannel(channel2);


            // Forecasting Channel
            NotificationChannel forecastingChannel = new NotificationChannel(
                    NotificationConstant.FORECASTING_CHANNEL_ID,
                    "Forecasting Channel",
                    NotificationManager.IMPORTANCE_HIGH
            );

            forecastingChannel.setDescription("This is forecasting suggestion channel");
            NotificationManager manager2 = getSystemService(NotificationManager.class);
            manager2.createNotificationChannel(forecastingChannel);
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
    NdefMessage[] getNdefMessagesFromIntent(Intent intent) {
        // Parse the intent
        NdefMessage[] msgs = null;
        String action = intent.getAction();
        if (action.equals(NfcAdapter.ACTION_TAG_DISCOVERED) || action.equals(NfcAdapter.ACTION_NDEF_DISCOVERED)) {
            Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
            if (rawMsgs != null) {
                msgs = new NdefMessage[rawMsgs.length];
                for (int i = 0; i < rawMsgs.length; i++) {
                    msgs[i] = (NdefMessage) rawMsgs[i];
                }

            } else {
                // Unknown tag type
                byte[] empty = new byte[]{};
                NdefRecord record = new NdefRecord(NdefRecord.TNF_UNKNOWN, empty, empty, empty);
                NdefMessage msg = new NdefMessage(new NdefRecord[]{record});
                msgs = new NdefMessage[]{msg};
            }

        } else {
            Log.e(TAG, "Unknown intent.");
            finish();
        }
        return msgs;
    }
}
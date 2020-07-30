package com.cs446.group18.timetracker.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.cs446.group18.timetracker.R;
import com.cs446.group18.timetracker.ReadTag;
import com.cs446.group18.timetracker.WriteTag;
import com.cs446.group18.timetracker.databinding.ActivityEventBinding;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity  {
    OnNewIntentListener newIntentListener;
    private DrawerLayout drawerLayout;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityEventBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_event);
        drawerLayout = binding.drawerLayout;

        setSupportActionBar(binding.toolbar);
        navController = Navigation.findNavController(this, R.id.event_nav_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);
        NavigationUI.setupWithNavController(binding.navigationView, navController);
        /*binding.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.write_tag_fragment:
                        startActivity(new Intent(MainActivity.this, WriteTag.class));
                        break;
                    case R.id.read_tag_fragment:
                        startActivity(new Intent(MainActivity.this, ReadTag.class));
                        break;
                }
                return false;
            }
        }); */

    }

    @Override
    protected void onNewIntent(Intent intent) {
        newIntentListener.onNewIntent(intent);
        super.onNewIntent(intent);
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


}
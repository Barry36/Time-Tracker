package com.cs446.group18.timetracker.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.databinding.DataBindingUtil;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;

import com.cs446.group18.timetracker.R;
import com.cs446.group18.timetracker.databinding.ActivityEventBinding;

public class MainActivity extends AppCompatActivity {
    private DrawerLayout drawerLayout;
    private NavController navController;
    private StopwatchFragment stopwatchFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityEventBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_event);
        drawerLayout = binding.drawerLayout;

        setSupportActionBar(binding.toolbar);
        navController = Navigation.findNavController(this, R.id.event_nav_fragment);
        stopwatchFragment = (StopwatchFragment) getSupportFragmentManager().findFragmentById(R.id.stopwatch_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);
        NavigationUI.setupWithNavController(binding.navigationView, navController);

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
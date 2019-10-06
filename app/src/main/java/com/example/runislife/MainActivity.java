package com.example.runislife;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.MenuItem;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;

    Fragment fragment = null;
    TextView textDist = null;
    TextView textSpeed = null;
    TextView textTime = null;
    RelativeLayout buttonAll;
    private CharSequence mTitle;
    private String[] navMenuTitles;
    BroadcastReceiver receiverHistory = null;
    BroadcastReceiver receiverConf = null;
    LocationManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        navMenuTitles = getResources().getStringArray(R.array.nav_drawer_items);
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Conf.init(getApplicationContext());
        buttonAll = findViewById(R.id.button_all);
        final Button button_fast_start = findViewById(R.id.button_fast_start);
        final Button button_stop = findViewById(R.id.button_stop);
        final Button button_chart = findViewById(R.id.button_chart);
        textDist = findViewById(R.id.button_distance);
        textSpeed = findViewById(R.id.button_speed);
        textTime = findViewById(R.id.button_time);
        IntentFilter filter = new IntentFilter(Record.MSG_RECORD_CHANGED);
        this.registerReceiver(receiverHistory, filter);
        button_fast_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, fast_start.class);
                startActivity(intent);
            }
        });
        button_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AllPlansActivity.class);
                startActivity(intent);
            }
        });
        button_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddTrainingActivity.class);
                startActivity(intent);
            }
        });
        checkLocationPermission();
    }

    public boolean checkLocationPermission() {
        if (((ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED)||(ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE))
                != PackageManager.PERMISSION_GRANTED)) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.ACCESS_FINE_LOCATION)&(ActivityCompat.shouldShowRequestPermissionRationale(this,
                    android.Manifest.permission.WRITE_EXTERNAL_STORAGE))){
                new AlertDialog.Builder(this)
                        .setPositiveButton(R.string.app_name, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ActivityCompat.requestPermissions(MainActivity.this,
                                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            backMainView();
            fragment = null;
            setTitle(navMenuTitles[0]);
        } else if (id == R.id.nav_plan) {
            fragment = new MyPlanFragment();
            disableEnableControls(false, buttonAll);
            setTitle(navMenuTitles[1]);
        } else if (id == R.id.nav_records) {
            fragment = new TimelineFragment();
            disableEnableControls(false, buttonAll);
            setTitle(navMenuTitles[2]);
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.frame_container, fragment).commit();
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void disableEnableControls(boolean enable, ViewGroup vg){
        for (int i = 0; i < vg.getChildCount(); i++){
            View child = vg.getChildAt(i);
            child.setEnabled(enable);
            if (enable)
                child.setVisibility(View.VISIBLE);
            else
                child.setVisibility(View.INVISIBLE);
            if (child instanceof ViewGroup){
                disableEnableControls(enable, (ViewGroup) child);
            }
        }
    }

    private void backMainView() {
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().remove(fragment).commit();
            fragment = null;
            setTitle(navMenuTitles[0]);
            disableEnableControls(true, buttonAll);
            getSupportActionBar().setTitle(mTitle);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        mTitle = title;
        getSupportActionBar().setTitle(mTitle);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(receiverHistory != null){
            this.unregisterReceiver(receiverHistory);
        }
        if(receiverConf != null){
            this.unregisterReceiver(receiverConf);
        }
    }
}

package com.example.runislife;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.Date;

public class fast_start extends AppCompatActivity implements LocationListener {
    com.google.android.gms.maps.SupportMapFragment mapFragment;
    GoogleMap map;
    Boolean stop = true;
    Boolean saved = true;
    Boolean firstStart = true;
    public double currentSpeed = 0;
    long totalTime = 0;
    long lastTime = 0;
    public double currentDistance = 0;
    Date startTime;
    int a=0;
    private LocationManager locationManager;
    ArrayList<GpsRec> locations = new ArrayList<>();
    TextView textDist = null;
    TextView textSpeed = null;
    TextView textTime = null;
    RelativeLayout buttonAll;
    private Handler mHandler = new Handler();
    public final static String EXTRA_GpsRec = "GpsRec";
    private static final String BC_INTENT = "BroadcastReceiver.location";
    LocationManager manager;
    public int d=0;
    ArrayList<Double> arrLat= new ArrayList<>();
    ArrayList<Double> arrLng = new ArrayList<>();
    double lat;
    double lng;

    public ArrayList<Parce> parces = new ArrayList<>();
    LinearRegression lr = new LinearRegression();
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fast_start);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setBackgroundDrawable(new ColorDrawable(getColor(R.color.colorPrimary)));
        actionBar.setTitle(Html.fromHtml("<font color='#ffffff'>Быстрый старт </font>"));
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Conf.init(getApplicationContext());
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                map = googleMap;
            }
        });
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        // Get a reference to the Press Me Button
        buttonAll = findViewById(R.id.button_all);
        final Button button_start = findViewById(R.id.button_start);
        final Button button_stop = findViewById(R.id.button_stop);
        final Button button_chart = findViewById(R.id.button_chart);

        textDist = findViewById(R.id.button_distance);
        textSpeed = findViewById(R.id.button_speed);
        textTime = findViewById(R.id.button_time);

        // Called each time the user clicks the Button

        button_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buildAlertMessageNoGps();
                if ( manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ==true ) {
                    lastTime = System.currentTimeMillis();
                    if (stop) {
                        if (startLocationUpdates()) {
                            stop = false;
                            button_chart.setClickable(true);
                            button_start.setText("Пауза");
                        }else{
                            return;
                        }
                    } else {
                        stop = true;
                        button_start.setText("Старт");
                        stopLocationUpdates();
                    }
                    if (firstStart) {
                        firstStart = false;
                        locations.clear();
                        currentDistance = 0;
                        currentSpeed = 0;
                        totalTime = 0;
                        saved = false;
                        startTime = new Date();
                    }}
            }
        });
        button_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stop = true;
                firstStart = true;
                // button_start.setText("Начать");
                stopLocationUpdates();
                if (!saved && locations.size()>0) {
                    saved = true;
                    Record record = new Record(getApplicationContext());
                    record.user = "default";
                    record.startTime = startTime;
                    record.usedTime = totalTime;
                    record.distance = currentDistance;
                    record.gpsRecs = locations;
                    record.save();
                    map.clear();
                    button_start.setText("Старт");
                    textDist.setText("0,00 Км");
                    textSpeed.setText("0,00 Км/ч");
                    textTime.setText("0:00:00");
                }else{
                    Toast.makeText(getBaseContext(), "Нет данных для записи", Toast.LENGTH_SHORT).show();
                }
            }


        });
        final Intent intent1 = new Intent(this,ChartActivity.class);

        button_chart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ii=textDist.getText().toString();
                if (textDist.getText().toString().equals("0,00 Км")){
                    // v.setClickable(false);
                    Toast.makeText(getBaseContext(), "Нет данных для отображения", Toast.LENGTH_LONG).show();


                }
                else
                {

                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList("Parce", parces);
                    intent1.putExtras(bundle);
                    startActivity(intent1);
                }
            }
        });

        // update the time
        Thread thread = new Thread()
        {
            @Override
            public void run() {
                try {
                    while(true) {
                        sleep(1000);
                        if (stop) continue;
                        totalTime += System.currentTimeMillis() - lastTime;
                        lastTime = System.currentTimeMillis();
                        long total_time_tmp = totalTime / 1000;
                        final String timeStr = String.format("%d:%02d:%02d", total_time_tmp / 3600, total_time_tmp % 3600 / 60, total_time_tmp % 3600 % 60);
                        mHandler.post(new Runnable() {
                            public void run() {
                                textTime.setText(timeStr);
                            }
                        });
                    }
                } catch (InterruptedException e) {}
            }
        };
        thread.start();
        };

   private void init() {
           CircleOptions circleOptions = new CircleOptions()
                   .center(new LatLng(arrLat.get(d),arrLng.get(d))).radius(3)
                   .fillColor(Color.YELLOW).strokeColor(Color.DKGRAY)
                   .strokeWidth(5);
           map.moveCamera(CameraUpdateFactory.newLatLngZoom(circleOptions.getCenter(), 16));
           map.addCircle(circleOptions);
           if (d >= 1) {
               PolylineOptions polygoneOptions = new PolylineOptions()
                       .add(new LatLng( arrLat.get(d), arrLng.get(d))).add(new LatLng(arrLat.get(d-1), arrLng.get(d-1)))
                       .color(Color.BLUE).width(10);
               map.addPolyline(polygoneOptions);
           }
           d++;
   }
    private void buildAlertMessageNoGps() {
        final LocationManager manager = (LocationManager) getSystemService( Context.LOCATION_SERVICE );if (manager.isProviderEnabled( LocationManager.GPS_PROVIDER ) ) {
            return;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.gps_prompt))
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, @SuppressWarnings("unused") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
    protected boolean startLocationUpdates() {
        try {
            manager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    Conf.INTERVAL_LOCATION * 1000L, Conf.MIN_DISTANCE * 0.1f, this);
            return true;
        } catch (SecurityException e) {
            return false;
        }catch (Exception eall) {
            return false;
        }
    }
    protected boolean stopLocationUpdates() {
        try {
            manager.removeUpdates(this);
            return true;
        } catch (SecurityException e) {
            return false;
        }catch (Exception eall) {
            return false;
        }
    }
    @Override
    public void onLocationChanged(Location location) {
        showLocation(location);
        saveLocation(location);
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras){}
    @Override
    public void onProviderEnabled(String provider){
        showLocation(locationManager.getLastKnownLocation(provider));
    }
    @Override
    public void onProviderDisabled(String provider){}
    long preLowAccuracyTime;
    int lowAccuracyCnt = 0;
    void saveLocation(Location l) {
        double dist = 0, speed = 0;
        double alt=0;
        if (l==null)return;
        Date date = new Date(l.getTime());
        if (Conf.GPS_ONLY && !l.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            return;
        }
        if (l.hasAccuracy() && l.getAccuracy() > Conf.LOCATION_ACCURACY) {
            if  (date.getTime()-preLowAccuracyTime < Conf.INTERVAL_LOCATION * 5 * 1000) {
                lowAccuracyCnt++;
            } else {
                if (lowAccuracyCnt > 2) {
                    mHandler.post(new Runnable() {
                        public void run() {
                            Toast.makeText(getApplicationContext(), String.format(getResources().getString(R.string.gps_precision_low2), Conf.LOCATION_ACCURACY), Toast.LENGTH_LONG).show();
                        }
                    });
                }
                lowAccuracyCnt = 0;
                preLowAccuracyTime = date.getTime();
            }
            return;
        }
        final GpsRec gps = new GpsRec(date, l);
        if (locations.size() >= 3-1) {
            lr.fix(locations,gps, 3);
            l.setLatitude(gps.lat);
            l.setLongitude(gps.lng);
        }
        if (locations.size() > 0) {
            GpsRec pre = locations.get(locations.size() - 1);
            dist = pre.loc.distanceTo(l);
            dist = Math.abs(dist);
            speed = dist / (1.0f * (date.getTime() - pre.getDate().getTime()) / 1000);
            if (l.hasAltitude()){
                alt = l.getAltitude();
            }else{
                alt = Conf.INVALID_ALT;
            }
        }
        if (Conf.SPEED_AVG > 0 && locations.size()>=Conf.SPEED_AVG-1) {
            GpsRec preN = locations.get(locations.size() - (Conf.SPEED_AVG - 1));
            double dist_avg = l.distanceTo(preN.loc);
            dist_avg = Math.abs(dist_avg);
            speed = dist_avg / (1.0f * (date.getTime() - preN.getDate().getTime()) / 1000);
        }
        gps.distance = dist;
        gps.speed = speed*3;
        gps.alt = alt;
        locations.add(gps);
        currentSpeed = speed;
        parces.add(a,new Parce(currentDistance,gps.speed));
        a++;
        currentDistance += dist;
        totalTime += date.getTime() - lastTime;
        lastTime = date.getTime();
        mHandler.post(new Runnable() {
            public void run() {
                textSpeed.setText(String.format("%s %s", Conf.getSpeedString(currentSpeed), Conf.getSpeedUnit()));
                textDist.setText(String.format("%.2f %s", Conf.getDistance(currentDistance), Conf.getDistanceUnit()));
                init();
            }
        });
        Intent msg = new Intent(BC_INTENT);
        msg.putExtra(EXTRA_GpsRec, gps);
        sendOrderedBroadcast(msg,null);
    }
    private void showLocation(Location location) {
        if (location == null)
            return;
        if (location.getProvider().equals(LocationManager.GPS_PROVIDER)) {
            formatLocation(location);
        }
    }
    private String formatLocation(Location location) {
        if (location == null)
            return "";
        lat = location.getLatitude();
        lng = location.getLongitude();
        System.out.println(lat);
        System.out.println(lng);
        arrLat.add(lat);
        arrLng.add(lng);
        return String.format(
                "Coordinates: lat = %1$.4f, lon = %2$.4f, time = %3$tF %3$tT",
                location.getLatitude(), location.getLongitude(), new Date(
                        location.getTime()));
    }
    @Override
    protected void onResume() {
        super.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

}

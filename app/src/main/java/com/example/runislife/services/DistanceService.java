package com.example.runislife.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.example.runislife.Conf;
import com.example.runislife.GpsRec;
import com.example.runislife.PlanActivity;
import com.example.runislife.R;
import com.example.runislife.Run;

import java.util.ArrayList;
import java.util.Date;

public class DistanceService extends Service {


    long totalTime = 0;
    long lastTime = 0;
    double currentSpeed = 0;
    double currentDistance = 0;
    ArrayList<GpsRec> locations = new ArrayList<>();
    long preLowAccuracyTime;
    int lowAccuracyCnt = 0;

    private LocationManager locationManager;

    public static final int SERVICE_ID = 200;
    private int idPlan = 0;
    private int id = 0;
    private String title = "";
    private String description = "";
    private int value = 0;
    private int progress = 0;
    private boolean running=false;

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent != null)
        {
            idPlan = intent.getIntExtra("id_plan", 0);
            id = intent.getIntExtra("id", 0);
            value = intent.getIntExtra("value", 0);
            title = intent.getStringExtra("title");
            description = intent.getStringExtra("description");
            if(!running){
                startRun();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void startRun(){
        startForeground(SERVICE_ID, createNotification());
        running = true;
        try{
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, Conf.INTERVAL_LOCATION * 1000L, Conf.MIN_DISTANCE * 0.1f, locationListener);
            new StartTask().execute();
        }
        catch (SecurityException e){
            e.printStackTrace();
            new CancelTask().execute();
            stopRun();
        }
    }

    private void stopRun(){
        stopForeground(true);
        stopSelf();
    }

    private Notification createNotification(){
        Intent notificationIntent = new Intent(this, PlanActivity.class);
        notificationIntent.putExtra("id", idPlan);
        notificationIntent.putExtra("preview", false);
        PendingIntent contentIntent = PendingIntent.getActivity(
                this, 0, notificationIntent, 0);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setContentTitle(title)
                .setContentText(description)
                .setSmallIcon(R.drawable.ic_run)
                .setProgress(value, progress, false)
                .setAutoCancel(false)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(contentIntent);
        return builder.build();
    }

    private void updateNotification(){
        Notification notification = createNotification();
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(SERVICE_ID, notification);
    }

    void updateLocation(Location l) {
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
                    // low accuracy warning
                }
                lowAccuracyCnt = 0;
                preLowAccuracyTime = date.getTime();
            }
            return;
        }
        final GpsRec gps = new GpsRec(date, l);
        if (locations.size() >= 3-1) {
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
        currentDistance += dist;
        totalTime += date.getTime() - lastTime;
        lastTime = date.getTime();
        if((int)currentDistance < value){
            progress = (int)currentDistance;
            updateNotification();
            new UpdateTask().execute();
        }
        else{
            progress = value;
            updateNotification();
            new FinishTask().execute();
            stopRun();
        }
    }

    private LocationListener locationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            updateLocation(location);
        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }
    };

    class StartTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            Run.instance.getDatabase().trainingDayExerciseDao().startExercise(id);
            return null;
        }
    }

    class UpdateTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            Run.instance.getDatabase().trainingDayExerciseDao().setProgress(progress, id);
            return null;
        }
    }

    class CancelTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            Run.instance.getDatabase().trainingDayExerciseDao().setProgress(0, id);
            Run.instance.getDatabase().trainingDayExerciseDao().resetExercise(id);
            return null;
        }
    }

    class FinishTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            Run.instance.getDatabase().trainingDayExerciseDao().setProgress(value, id);
            Run.instance.getDatabase().trainingDayExerciseDao().passExercise(id);
            return null;
        }
    }
}

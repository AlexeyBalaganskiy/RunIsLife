package com.example.runislife.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.example.runislife.PlanActivity;
import com.example.runislife.R;
import com.example.runislife.Run;

public class TimerService extends Service {

    public static final int SERVICE_ID = 100;
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
                startTimer();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    private void startTimer(){
        startForeground(SERVICE_ID, createNotification());
        running = true;
        new CountDownTimer(value*1000, 1000) {

            public void onTick(long millisUntilFinished) {
                progress = value - (int) (millisUntilFinished / 1000);
                updateNotification();
                new UpdateTask().execute();
            }

            public void onFinish() {
                progress = value;
                updateNotification();
                new FinishTask().execute();
                stopTimer();
            }
        }.start();
        new StartTask().execute();
    }

    private void stopTimer(){
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

    class FinishTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {
            Run.instance.getDatabase().trainingDayExerciseDao().setProgress(value, id);
            Run.instance.getDatabase().trainingDayExerciseDao().passExercise(id);
            return null;
        }
    }

}
